package com.wooteco.sokdak.member.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class InvalidAuthCodeException extends BadRequestException {

    private static final String MESSAGE = "잘못된 인증번호입니다.";

    public InvalidAuthCodeException() {
        super(MESSAGE);
    }
}
