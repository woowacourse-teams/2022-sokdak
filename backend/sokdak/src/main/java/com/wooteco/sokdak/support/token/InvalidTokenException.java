package com.wooteco.sokdak.support.token;

import com.wooteco.sokdak.advice.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
