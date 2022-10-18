package com.wooteco.sokdak.report.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReportRequest {

    @NotBlank(message = "신고내용은 1자 이상 255자 이하여야 합니다.")
    private String message;

    public ReportRequest() {
    }

    public ReportRequest(String message) {
        this.message = message;
    }
}
