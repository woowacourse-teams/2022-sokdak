package com.wooteco.sokdak.report.acceptance;

import static com.wooteco.sokdak.util.fixture.CommentFixture.*;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.*;
import static com.wooteco.sokdak.util.fixture.PostFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("댓글 신고 관련 인수테스트")
class CommentReportAcceptanceTest extends AcceptanceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜댓글");

    @DisplayName("댓글을 신고할 수 있다.")
    @Test
    void reportComment() {
        Long commentId = addCommentAndGetCommentId();

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 신고한 댓글은 다시 신고할 수 없다.")
    @Test
    void reportComment_Exception_AlreadyReport() {
        addPostAndGetPostId();
        Long commentId = addCommentAndGetCommentId();
        httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getToken());

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("없는 댓글은 신고할 수 없다.")
    @Test
    void reportComment_Exception_NotFoundComment() {
        addPostAndGetPostId();
        long invalidCommentId = 9999L;

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + invalidCommentId + "/report", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Long addCommentAndGetCommentId() {
        String token = getToken();
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getToken()));

        NewCommentRequest newCommentRequest = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
        Long commentId = Long.parseLong(
                httpPostWithAuthorization(newCommentRequest, "/posts/" + postId + "/comments", token)
                        .header("Location").split("/comments/")[1]);

        return commentId;
    }

    private String parsePostId(ExtractableResponse<Response> response) {
        return response.header("Location")
                .split("/posts/")[1];
    }
}
