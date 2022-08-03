package com.wooteco.sokdak.comment.acceptance;

import static com.wooteco.sokdak.post.util.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.post.util.PostFixture.NEW_POST_REQUEST;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.PostFixture.CREATE_POST_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CommentAcceptanceTest extends AcceptanceTest {

    private static final NewCommentRequest NEW_ANONYMOUS_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
    private static final NewCommentRequest NEW_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, false);

    @DisplayName("새로운 익명 댓글을 작성할 수 있다.")
    @Test
    void addComment_Anonymous() {
        Long postId = addPostAndGetPostId();

        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("새로운 기명 댓글을 작성할 수 있다.")
    @Test
    void addComment_Nickname() {
        Long postId = addPostAndGetPostId();

        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인하지 않고, 댓글을 작성할 수 없다.")
    @Test
    void addComment_Unauthorized() {
        Long postId = addPostAndGetPostId();

        ExtractableResponse<Response> response = httpPost(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + postId + "/comments");

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("특정 게시글의 댓글을 조회할 수 있다.")
    @Test
    void findComments() {
        Long postId = addPostAndGetPostId();
        Long otherPostId = addPostAndGetPostId();
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getToken());
        httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getToken());
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + otherPostId + "/comments",
                getToken());

        ExtractableResponse<Response> response = httpGet("/posts/" + postId + "/comments");
        List<CommentResponse> commentResponses = response
                .jsonPath()
                .getObject(".", CommentsResponse.class)
                .getComments();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(commentResponses.size()).isEqualTo(2)
        );
    }

    @DisplayName("누적 신고가 5개 이상인 댓글은 block된다.")
    @Test
    void findComments_Block() {
        Long postId = addPostAndGetPostId();
        Long commentId = addCommentAndGetCommentId(postId);
        addCommentAndGetCommentId(postId);
        String token1 = getToken();
        String token2 = httpPost(new LoginRequest("josh", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token3 = httpPost(new LoginRequest("thor", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token4 = httpPost(new LoginRequest("hunch", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token5 = httpPost(new LoginRequest("east", "Abcd123!@"), "/login").header(AUTHORIZATION);
        List<String> tokens = List.of(token1, token2, token3, token4, token5);

        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("댓글신고");
            httpPostWithAuthorization(reportRequest, "/comments/" + commentId + "/report", tokens.get(i));
        }

        ExtractableResponse<Response> response = httpGet("/posts/" + postId + "/comments");
        List<CommentResponse> commentResponses = response.jsonPath().getObject(".", CommentsResponse.class).getComments();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(commentResponses.get(0).isBlocked()).isTrue(),
                () -> assertThat(commentResponses.get(1).isBlocked()).isFalse()
        );
    }

    @DisplayName("댓글을 삭제할 수 있다.")
    @Test
    void deleteComment() {
        Long postId = addPostAndGetPostId();
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", getToken());

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/comments/1", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    private Long addPostAndGetPostId() {
        return Long.parseLong(httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getToken())
                .header("Location").split("/posts/")[1]);
    }

    private Long addCommentAndGetCommentId(Long postId) {
        NewCommentRequest newCommentRequest = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
        return Long.parseLong(httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments", getToken())
                .header("Location").split("/comments/")[1]);
    }
}
