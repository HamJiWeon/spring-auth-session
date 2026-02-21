package com.session.auth.service;

import com.session.auth.dto.SignUpRequest;
import com.session.auth.entity.Member;
import com.session.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {
        validateDuplicateMember(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = request.toEntity(encodedPassword);
        return memberRepository.save(member).getId();
    }

    public Member login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    private void validateDuplicateMember(SignUpRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalStateException("이미 사용 중인 아이디 입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일 입니다.");
        }
    }
}
