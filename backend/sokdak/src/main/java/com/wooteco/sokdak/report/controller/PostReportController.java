package com.wooteco.sokdak.report.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.service.PostReportService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostReportController {

    private final PostReportService postReportService;

    public PostReportController(PostReportService postReportService) {
        this.postReportService = postReportService;
    }

    @PostMapping("/posts/{id}/report")
    public ResponseEntity<Void> reportPost(@PathVariable(name = "id") Long postId,
                                           @RequestBody ReportRequest reportRequest,
                                           @Login AuthInfo authInfo) {
        postReportService.reportPost(postId, reportRequest, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
