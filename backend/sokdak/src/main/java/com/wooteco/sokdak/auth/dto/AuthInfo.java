package com.wooteco.sokdak.auth.dto;

import lombok.Getter;

@Getter
public class AuthInfo {

    private Long id;

    public AuthInfo(Long id) {
        this.id = id;
    }
}
