package com.wooteco.sokdak.report.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith(RestDocumentationExtension.class)
class CommentReportControllerTest extends ControllerTest {

    @DisplayName("댓글을 신고한다")
    @Test
    void reportComment() {
        ReportRequest reportRequest = new ReportRequest("나쁜댓글");
        doNothing()
                .when(commentReportService)
                .reportComment(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(reportRequest)
                .when().post("/comments/2/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 신고한 댓글을 다시 신고하면 400을 반환한다.")
    @Test
    void reportComment_Exception_AlreadyReport() {
        ReportRequest reportRequest = new ReportRequest("나쁜댓글");
        doThrow(new AlreadyReportCommentException())
                .when(commentReportService)
                .reportComment(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(reportRequest)
                .when().post("/comments/1/report")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("신고 내용 없이 댓글을 신고하면 400을 반환한다")
    @Test
    void reportPost_Exception_NoContent() {
        ReportRequest reportRequest = new ReportRequest("  ");
        doThrow(new InvalidReportMessageException())
                .when(commentReportService)
                .reportComment(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(reportRequest)
                .when().post("/comments/1/report")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
