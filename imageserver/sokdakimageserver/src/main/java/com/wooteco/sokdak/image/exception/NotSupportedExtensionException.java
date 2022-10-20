package com.wooteco.sokdak.image.exception;

public class NotSupportedExtensionException extends RuntimeException {

    private static final String MESSAGE = "지원하지 않는 확장자입니다!";

    public NotSupportedExtensionException() {
        super(MESSAGE);
    }
}
