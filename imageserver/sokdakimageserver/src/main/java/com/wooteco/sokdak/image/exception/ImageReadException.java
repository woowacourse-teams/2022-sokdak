package com.wooteco.sokdak.image.exception;

public class ImageReadException extends RuntimeException {

    private static final String MESSAGE = "이미지를 읽어오는데 문제가 발생했습니다.";

    public ImageReadException() {
        super(MESSAGE);
    }
}
