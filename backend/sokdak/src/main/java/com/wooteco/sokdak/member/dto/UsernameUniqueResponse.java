package com.wooteco.sokdak.member.dto;

import lombok.Getter;

@Getter
public class UsernameUniqueResponse {

    private final boolean success;

    public UsernameUniqueResponse(boolean success) {
        this.success = success;
    }
}
