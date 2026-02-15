package com.session.auth.service;

import com.session.auth.dto.MemberDto;
import com.session.auth.entity.Member;
import com.session.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(MemberDto memberDto) {

        validateDuplicateMember(memberDto.getUserEmail());

        String encodedPassword = passwordEncoder.encode(memberDto.getUserPassword());


        Member member = Member.builder()
                .userEmail(memberDto.getUserEmail())
                .userPassword(encodedPassword)
                .userPhone(memberDto.getUserPhone())
                .userBirthDate(memberDto.getUserBirthDate())
                .role(memberDto.getRole() != null ? memberDto.getRole() : "USER")
                .build();

        return memberRepository.save(member).getUserId();
    }

    private void validateDuplicateMember(String userEmail) {
        memberRepository.findByUserEmail(userEmail)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                });
    }

    @Transactional(readOnly = true)
    public Long login(String email, String password) {
        Member member = memberRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(password, member.getUserPassword())) {
            throw new IllegalStateException("일치하지 않는 비밀번호입니다.");
        }

        return member.getUserId();
    }

    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(oldPassword, member.getUserPassword())) {
            throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        member.updatePassword(encodedNewPassword);
    }

    @Transactional
    public void deleteMember(Long userId) {
        memberRepository.deleteById(userId);
    }
}
