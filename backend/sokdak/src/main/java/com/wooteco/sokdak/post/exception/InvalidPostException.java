package com.wooteco.sokdak.post.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class InvalidPostException extends BadRequestException {

    private static final String MESSAGE = "제목 혹은 본문이 없습니다.";

    public InvalidPostException() {
        super(MESSAGE);
    }
}
