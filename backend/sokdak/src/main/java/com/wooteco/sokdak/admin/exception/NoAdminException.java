package com.wooteco.sokdak.admin.exception;

import com.wooteco.sokdak.advice.UnauthorizedException;

public class NoAdminException extends UnauthorizedException {

    private static final String MESSAGE = "관리자만 접근 가능합니다.";

    public NoAdminException() {
        super(MESSAGE);
    }
}
