package com.session.auth.controller;

import com.session.auth.dto.MemberDto;
import com.session.auth.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // Test
    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo(
            @SessionAttribute(name = "loginMember", required = false) Long userId
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        return ResponseEntity.ok("현재 로그인 된 회원 ID: " + userId);
    }
}
