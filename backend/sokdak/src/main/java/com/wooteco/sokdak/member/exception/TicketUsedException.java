package com.wooteco.sokdak.member.exception;

import com.wooteco.sokdak.post.exception.BadRequestException;

public class TicketUsedException extends BadRequestException {

    private static final String MESSAGE = "이미 가입된 크루입니다.";

    public TicketUsedException() {
        super(MESSAGE);
    }
}
