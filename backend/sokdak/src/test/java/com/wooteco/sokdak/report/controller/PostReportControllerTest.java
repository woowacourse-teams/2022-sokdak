package com.wooteco.sokdak.report.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.util.ControllerTest;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith(RestDocumentationExtension.class)
class PostReportControllerTest extends ControllerTest {

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
    }

    @DisplayName("게시글을 신고한다")
    @Test
    void reportPost() {
        ReportRequest reportRequest = new ReportRequest("나쁜글");
        doNothing()
                .when(postReportService)
                .reportPost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(reportRequest)
                .when().post("/posts/2/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 신고한 게시물을 다시 신고하면 400을 반환한다.")
    @Test
    void reportPost_Exception_AlreadyReport() {
        ReportRequest reportRequest = new ReportRequest("나쁜글");
        doThrow(new AlreadyReportPostException())
                .when(postReportService)
                .reportPost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(reportRequest)
                .when().post("/posts/1/report")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("신고 내용 없이 게시물을 신고하면 400을 반환한다")
    @Test
    void reportPost_Exception_NoContent() {
        ReportRequest reportRequest = new ReportRequest("  ");
        doThrow(new InvalidReportMessageException())
                .when(postReportService)
                .reportPost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(reportRequest)
                .when().post("/posts/1/report")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
