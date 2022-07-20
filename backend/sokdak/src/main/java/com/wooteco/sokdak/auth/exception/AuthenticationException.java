package com.wooteco.sokdak.auth.exception;

import com.wooteco.sokdak.advice.ForbiddenException;

public class AuthenticationException extends ForbiddenException {

    private static final String MESSAGE = "권한이 없습니다.";

    public AuthenticationException() {
        super(MESSAGE);
    }
}
