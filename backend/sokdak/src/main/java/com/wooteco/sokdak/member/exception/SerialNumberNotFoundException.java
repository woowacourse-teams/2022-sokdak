package com.wooteco.sokdak.member.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class SerialNumberNotFoundException extends BadRequestException {

    private static final String MESSAGE = "인증번호를 발급하지 않은 이메일입니다.";

    public SerialNumberNotFoundException() {
        super(MESSAGE);
    }
}
