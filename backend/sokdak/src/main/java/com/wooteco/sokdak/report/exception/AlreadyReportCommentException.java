package com.wooteco.sokdak.report.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class AlreadyReportCommentException extends BadRequestException {

    private static final String MESSAGE = "이미 신고한 댓글입니다.";

    public AlreadyReportCommentException() {
        super(MESSAGE);
    }
}
