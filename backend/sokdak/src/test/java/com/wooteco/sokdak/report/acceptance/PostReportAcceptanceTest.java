package com.wooteco.sokdak.report.acceptance;

import static com.wooteco.sokdak.util.fixture.BoardFixture.*;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import com.wooteco.sokdak.util.fixture.BoardFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("게시글 신고 관련 인수테스트")
class PostReportAcceptanceTest extends AcceptanceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest(FREE_BOARD_ID, "나쁜글");

    @DisplayName("게시글을 신고할 수 있다.")
    @Test
    void reportPost() {
        Long postId = addNewPost();

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/posts/" + postId + "/report", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 신고한 게시물은 다시 신고할 수 없다.")
    @Test
    void reportPost_Exception_AlreadyReport() {
        Long postId = addNewPost();
        httpPostWithAuthorization(REPORT_REQUEST, "/posts/" + postId + "/report", getChrisToken());

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/posts/" + postId + "/report", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("없는 게시글을 신고할 순 없다.")
    @Test
    void reportPost_Exception_NotFoundPost() {
        long invalidPostId = 9999L;

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/posts/" + invalidPostId + "/report", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
