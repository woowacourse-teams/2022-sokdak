package com.wooteco.sokdak.auth.dto;

import lombok.Getter;

@Getter
public class AuthInfo {

    private Long id;
    private String role;
    private String nickname;

    public AuthInfo(Long id, String role, String nickname) {
        this.id = id;
        this.role = role;
        this.nickname = nickname;
    }
}
