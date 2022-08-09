package com.wooteco.sokdak.profile.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EditedNicknameRequest {
    @NotBlank(message = "닉네임은 1자 이상이어야 합니다.")
    private String nickname;

    public EditedNicknameRequest() {
    }

    public EditedNicknameRequest(String nickname) {
        this.nickname = nickname;
    }
}
