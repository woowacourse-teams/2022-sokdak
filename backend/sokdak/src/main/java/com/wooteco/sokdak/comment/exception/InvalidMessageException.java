package com.wooteco.sokdak.comment.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class InvalidMessageException extends BadRequestException {

    private static final String MESSAGE = "댓글은 1자 이상 255자 이하여야 합니다.";

    public InvalidMessageException() {
        super(MESSAGE);
    }
}
