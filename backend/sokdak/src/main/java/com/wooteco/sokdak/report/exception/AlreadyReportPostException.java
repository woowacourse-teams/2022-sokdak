package com.wooteco.sokdak.report.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class AlreadyReportPostException extends BadRequestException {

    private static final String MESSAGE = "이미 신고한 게시물입니다.";

    public AlreadyReportPostException() {
        super(MESSAGE);
    }
}
