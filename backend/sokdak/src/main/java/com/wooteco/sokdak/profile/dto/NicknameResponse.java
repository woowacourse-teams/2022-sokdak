package com.wooteco.sokdak.profile.dto;

import lombok.Getter;

@Getter
public class NicknameResponse {
    private String nickname;

    public NicknameResponse() {
    }

    public NicknameResponse(String nickname) {
        this.nickname = nickname;
    }
}
