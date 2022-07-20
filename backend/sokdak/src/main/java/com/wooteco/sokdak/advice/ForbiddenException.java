package com.wooteco.sokdak.advice;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message);
    }
}
