package com.wooteco.sokdak.report.domain;

import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ReportMessage {

    private static final int MAX_MESSAGE_LENGTH = 255;

    @Column(name = "report_message", nullable = false)
    private String value;

    protected ReportMessage() {
    }

    public ReportMessage(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidReportMessageException();
        }
        if (value.length() > MAX_MESSAGE_LENGTH) {
            throw new InvalidReportMessageException();
        }
    }
}
