package com.wooteco.sokdak.member.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class NotWootecoMemberException extends BadRequestException {

    private static final String MESSAGE = "우아한테크코스 크루가 아닙니다.";

    public NotWootecoMemberException() {
        super(MESSAGE);
    }
}
