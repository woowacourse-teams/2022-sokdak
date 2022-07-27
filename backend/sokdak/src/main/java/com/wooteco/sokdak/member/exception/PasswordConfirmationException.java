package com.wooteco.sokdak.member.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class PasswordConfirmationException extends BadRequestException {

    private static final String MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";

    public PasswordConfirmationException() {
        super(MESSAGE);
    }
}
