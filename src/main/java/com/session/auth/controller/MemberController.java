package com.session.auth.controller;

import com.session.auth.dto.MemberDto;
import com.session.auth.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberDto memberDto) {
        try {
            Long userId = memberService.save(memberDto);
            return ResponseEntity.ok("회원가입이 정상적으로 완료되었습니다. ID: " + userId);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDto memberDto, HttpServletRequest request) {
        try {
            Long userId = memberService.login(memberDto.getUserEmail(), memberDto.getUserPassword());

            HttpSession session = request.getSession();
            session.setAttribute("loginMember", userId);

            return ResponseEntity.ok("로그인 성공. ID: " + userId);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
