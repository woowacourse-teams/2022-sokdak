package com.wooteco.sokdak.member.dto;

import lombok.Getter;

@Getter
public class SignupRequest {

    private String email;
    private String username;
    private String nickname;
    private String code;
    private String password;
    private String passwordConfirmation;

    public SignupRequest() {
    }

    public SignupRequest(String email, String username, String nickname, String code, String password,
                         String passwordConfirmation) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.code = code;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}
