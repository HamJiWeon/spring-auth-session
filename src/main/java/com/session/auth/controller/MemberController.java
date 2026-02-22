package com.session.auth.controller;

import com.session.auth.dto.LoginRequest;
import com.session.auth.dto.MemberResponse;
import com.session.auth.dto.SignUpRequest;
import com.session.auth.entity.Member;
import com.session.auth.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody SignUpRequest request) {
        Long memberId = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        Member loginMember = memberService.login(loginRequest.getLoginId(), loginRequest.getPassword());

        HttpSession session = request.getSession();

        session.setAttribute("loginMember", MemberResponse.from(loginMember));

        return ResponseEntity.ok(MemberResponse.from(loginMember));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        return ResponseEntity.ok().build();
    }
}
