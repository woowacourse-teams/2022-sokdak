package com.wooteco.sokdak.report.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.service.CommentReportService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentReportController {

    private final CommentReportService commentReportService;

    public CommentReportController(CommentReportService commentReportService) {
        this.commentReportService = commentReportService;
    }

    @PostMapping("/comments/{id}/report")
    public ResponseEntity<Void> commentPost(@PathVariable(name = "id") Long commentId,
                                            @RequestBody ReportRequest reportRequest,
                                            @Login AuthInfo authInfo) {
        commentReportService.reportComment(commentId, reportRequest, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
