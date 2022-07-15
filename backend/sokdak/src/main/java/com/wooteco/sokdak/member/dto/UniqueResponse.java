package com.wooteco.sokdak.member.dto;

import lombok.Getter;

@Getter
public class UniqueResponse {

    private final boolean unique;

    public UniqueResponse(boolean unique) {
        this.unique = unique;
    }
}
