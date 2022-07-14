package com.wooteco.sokdak.member.dto;

import lombok.Getter;

@Getter
public class UsernameUniqueResponse {

    private final boolean unique;

    public UsernameUniqueResponse(boolean unique) {
        this.unique = unique;
    }
}
