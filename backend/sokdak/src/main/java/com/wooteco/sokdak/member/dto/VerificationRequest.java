package com.wooteco.sokdak.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerificationRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String code;

    public VerificationRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public VerificationRequest() {
    }
}
