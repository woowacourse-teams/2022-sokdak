package com.wooteco.sokdak.advice;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message);
    }
}
