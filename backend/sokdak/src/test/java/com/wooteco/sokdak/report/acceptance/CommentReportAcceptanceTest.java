package com.wooteco.sokdak.report.acceptance;

import static com.wooteco.sokdak.util.fixture.BoardFixture.*;
import static com.wooteco.sokdak.util.fixture.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;

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

    private static final ReportRequest REPORT_REQUEST = new ReportRequest(FREE_BOARD_ID, "나쁜댓글");

    @DisplayName("댓글을 신고할 수 있다.")
    @Test
    void reportComment() {
        Long commentId = addCommentAndGetCommentId();

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 신고한 댓글은 다시 신고할 수 없다.")
    @Test
    void reportComment_Exception_AlreadyReport() {
        addNewPost();
        Long commentId = addCommentAndGetCommentId();
        httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getChrisToken());

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("없는 댓글은 신고할 수 없다.")
    @Test
    void reportComment_Exception_NotFoundComment() {
        addNewPost();
        long invalidCommentId = 9999L;

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + invalidCommentId + "/report", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Long addCommentAndGetCommentId() {
        String token = getChrisToken();
        Long postId = addNewPost();

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
