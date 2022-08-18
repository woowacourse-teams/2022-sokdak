package com.wooteco.sokdak.auth.exception;

import com.wooteco.sokdak.advice.UnauthorizedException;

public class LoginFailedException extends UnauthorizedException {

    private static final String MESSAGE = "아이디나 비밀번호가 잘못되었습니다";

    public LoginFailedException() {
        super(MESSAGE);
    }
}
