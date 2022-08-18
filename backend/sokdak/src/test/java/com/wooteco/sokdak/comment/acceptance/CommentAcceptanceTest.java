package com.wooteco.sokdak.comment.acceptance;

import static com.wooteco.sokdak.util.fixture.CommentFixture.NEW_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.util.fixture.CommentFixture.addNewCommentInPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getTokens;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
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

    @DisplayName("새로운 익명 댓글을 작성할 수 있다.")
    @Test
    void addComment_Anonymous() {
        Long postId = addNewPost();

        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("새로운 기명 댓글을 작성할 수 있다.")
    @Test
    void addComment_Nickname() {
        Long postId = addNewPost();

        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인하지 않고, 댓글을 작성할 수 없다.")
    @Test
    void addComment_Unauthorized() {
        Long postId = addNewPost();

        ExtractableResponse<Response> response = httpPost(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + postId + "/comments");

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("특정 게시글의 댓글을 조회할 수 있다.")
    @Test
    void findComments() {
        Long postId = addNewPost();
        Long otherPostId = addNewPost();
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getChrisToken());
        httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments",
                getChrisToken());
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/posts/" + otherPostId + "/comments",
                getChrisToken());

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

    @DisplayName("댓글 목록을 조회하면 총 댓글 개수도 조회할 수 있다.")
    @Test
    void findComments_With_TotalCount() {
        Long postId = addNewPost();
        Long commentId = addNewCommentInPost(postId);
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/comments/" + commentId + "/reply",
                getChrisToken());
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/comments/" + commentId + "/reply",
                getChrisToken());

        ExtractableResponse<Response> response = httpGet("/posts/" + postId + "/comments");
        CommentsResponse commentsResponse = response
                .jsonPath()
                .getObject(".", CommentsResponse.class);

        assertThat(commentsResponse.getTotalCount()).isEqualTo(3);
    }

    @DisplayName("누적 신고가 5개 이상인 댓글은 block된다.")
    @Test
    void findComments_Block() {
        Long postId = addNewPost();
        Long commentId = addNewCommentInPost(postId);
        addNewCommentInPost(postId);
        List<String> tokens = getTokens();

        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("댓글신고");
            httpPostWithAuthorization(reportRequest, "/comments/" + commentId + "/report", tokens.get(i));
        }

        ExtractableResponse<Response> response = httpGet("/posts/" + postId + "/comments");
<<<<<<< HEAD
        List<CommentResponse> commentResponses = response.jsonPath().getObject(".", CommentsResponse.class)
=======
        List<CommentResponse> commentResponses = response.jsonPath()
                .getObject(".", CommentsResponse.class)
>>>>>>> b5db00c (feat: 댓글 좋아요 기능 #395 (#501))
                .getComments();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(commentResponses.get(0).isBlocked()).isTrue(),
                () -> assertThat(commentResponses.get(1).isBlocked()).isFalse()
        );
    }

    @DisplayName("특정 게시글의 댓글과 대댓글을 조회할 수 있다.")
    @Test
    void findComments_With_Replies() {
        Long postId = addNewPost();
        Long commentId = addNewCommentInPost(postId);
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/comments/" + commentId + "/reply",
                getChrisToken());
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/comments/" + commentId + "/reply",
                getChrisToken());

        ExtractableResponse<Response> response = httpGet("/posts/" + postId + "/comments");
        List<CommentResponse> commentResponses = response
                .jsonPath()
                .getObject(".", CommentsResponse.class)
                .getComments();
        List<ReplyResponse> replyResponses = commentResponses.get(0).getReplies();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(commentResponses).hasSize(1),
                () -> assertThat(replyResponses).hasSize(2)
        );
    }

    @DisplayName("댓글을 삭제할 수 있다.")
    @Test
    void deleteComment() {
        Long postId = addNewPost();
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", getChrisToken());

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/comments/1", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("대댓글을 가진 댓글을 삭제하고 해당 게시글의 댓글을 조회하면 대댓글만 조회할 수 있다.")
    @Test
    void deleteComment_Having_Reply() {
        Long postId = addNewPost();
        Long commentId = addNewCommentInPost(postId);
        httpPostWithAuthorization(NEW_ANONYMOUS_COMMENT_REQUEST,
                "/comments/" + commentId + "/reply",
                getChrisToken());

        httpDeleteWithAuthorization("/comments/" + commentId, getChrisToken());
        ExtractableResponse<Response> response = httpGet("/posts/" + postId + "/comments");
        CommentResponse commentResponse = response.jsonPath()
                .getObject(".", CommentsResponse.class)
                .getComments().get(0);

        List<ReplyResponse> replyResponses = commentResponse.getReplies();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(commentResponse.getId()).isEqualTo(commentId),
                () -> assertThat(commentResponse.getContent()).isNull(),
                () -> assertThat(commentResponse.getNickname()).isNull(),
                () -> assertThat(replyResponses).hasSize(1)
        );
    }
}
