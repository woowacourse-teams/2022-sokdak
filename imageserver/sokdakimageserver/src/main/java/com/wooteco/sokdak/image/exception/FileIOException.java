package com.wooteco.sokdak.image.exception;

import com.wooteco.sokdak.advice.InternalException;

public class FileIOException extends InternalException {

    private static final String MESSAGE = "파일 저장 중 오류가 발생했습니다.";

    public FileIOException() {
        super(MESSAGE);
    }
}
