package com.wooteco.sokdak.admin.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostReportsResponse {

    private List<PostReportElement> postReports;

    protected PostReportsResponse() {
    }

    public PostReportsResponse(List<PostReportElement> postReports) {
        this.postReports = postReports;
    }

    public static PostReportsResponse of(List<PostReportElement> postReportElements) {
        return new PostReportsResponse(postReportElements);
    }
}
