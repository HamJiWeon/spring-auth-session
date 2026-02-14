package com.session.auth.service;

import com.session.auth.dto.MemberDto;
import com.session.auth.entity.Member;
import com.session.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
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
}
