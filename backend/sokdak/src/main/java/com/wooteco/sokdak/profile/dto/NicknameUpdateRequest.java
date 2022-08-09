package com.wooteco.sokdak.profile.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameUpdateRequest {

    @NotBlank(message = "닉네임은 1자 이상이어야 합니다.")
    private String nickname;

    public NicknameUpdateRequest() {
    }

    public NicknameUpdateRequest(String nickname) {
        this.nickname = nickname;
    }
}
