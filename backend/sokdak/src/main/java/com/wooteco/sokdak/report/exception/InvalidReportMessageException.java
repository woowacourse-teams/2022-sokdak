package com.wooteco.sokdak.report.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class InvalidReportMessageException extends BadRequestException {

    private static final String MESSAGE = "신고내용은 1자 이상 255자 이하여야 합니다.";

    public InvalidReportMessageException() {
        super(MESSAGE);
    }
}
