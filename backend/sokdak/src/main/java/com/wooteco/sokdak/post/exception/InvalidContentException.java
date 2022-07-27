package com.wooteco.sokdak.post.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class InvalidContentException extends BadRequestException {

    private static final String MESSAGE = "본문은 1자 이상 5000자 이하여야 합니다.";

    public InvalidContentException() {
        super(MESSAGE);
    }
}
