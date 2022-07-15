package com.wooteco.sokdak.post.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class InvalidTitleException extends BadRequestException {

    private static final String MESSAGE = "제목은 1자 이상 50자 이하여야 합니다.";

    public InvalidTitleException() {
        super(MESSAGE);
    }
}
