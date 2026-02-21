package com.session.auth.dto;

import com.session.auth.entity.Member;
import com.session.auth.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String loginId;
    private String email;
    private Role role;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
