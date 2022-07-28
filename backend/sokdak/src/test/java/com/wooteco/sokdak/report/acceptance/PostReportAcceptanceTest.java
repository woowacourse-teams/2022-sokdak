package com.wooteco.sokdak.report.acceptance;

import static com.wooteco.sokdak.post.util.PostFixture.*;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("게시글 신고 관련 인수테스트")
class PostReportAcceptanceTest extends AcceptanceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜글");

    @DisplayName("게시글을 신고할 수 있다.")
    @Test
    void reportPost() {
        Long postId = addPostAndGetPostId();

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/posts/" + postId + "/report", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    private Long addPostAndGetPostId() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
                Collections.emptyList());
        return Long.parseLong(httpPostWithAuthorization(newPostRequest, "/posts", getToken())
                .header("Location").split("/posts/")[1]);
    }
}
