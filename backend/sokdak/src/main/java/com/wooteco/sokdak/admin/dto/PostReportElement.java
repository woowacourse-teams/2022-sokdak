package com.wooteco.sokdak.admin.dto;

import com.wooteco.sokdak.report.domain.PostReport;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostReportElement {

    private Long id;
    private Long postId;
    private Long reporterId;
    private String reportMessage;
    private LocalDateTime createdAt;

    protected PostReportElement(){
    }

    @Builder
    public PostReportElement(Long id, Long postId, Long reporterId, String reportMessage, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.reporterId = reporterId;
        this.reportMessage = reportMessage;
        this.createdAt = createdAt;
    }

    public static PostReportElement of(PostReport postReport) {
        return PostReportElement.builder()
                .id(postReport.getId())
                .postId(postReport.getPost().getId())
                .reporterId(postReport.getReporter().getId())
                .reportMessage(postReport.getReportMessage())
                .createdAt(postReport.getCreatedAt())
                .build();
    }
}
