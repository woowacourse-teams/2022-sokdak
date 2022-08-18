package com.wooteco.sokdak.like.acceptance;

<<<<<<< HEAD
=======
import static com.wooteco.sokdak.util.fixture.CommentFixture.NEW_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getJoshToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
>>>>>>> b5db00c (feat: 댓글 좋아요 기능 #395 (#501))
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class LikeAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인한 회원은 좋아요 하지 않은 게시물에 좋아요를 할 수 있다.")
    @Test
    void flipLike_Create() {
        Long postId = addNewPost();

        ExtractableResponse<Response> response = httpPutWithAuthorization("/posts/" + postId + "/like",
                getChrisToken());
        LikeFlipResponse likeFlipResponse = response.jsonPath().getObject(".", LikeFlipResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("로그인한 회원이 좋아요를 누른 게시물에 좋아요를 취소할 수 있다.")
    @Test
    void flipLike_Delete() {
        String token = getChrisToken();
        Long postId = addNewPost();

        httpPutWithAuthorization("/posts/" + postId + "/like", token);

        ExtractableResponse<Response> response = httpPutWithAuthorization("/posts/" + postId + "/like", token);
        LikeFlipResponse likeFlipResponse = response.jsonPath().getObject(".", LikeFlipResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("로그인 하지 않은 회원이 게시글에 좋아요를 누를 경우 예외를 반환한다")
    @Test
    void flipLike_Unauthorized() {
        Long postId = addNewPost();

        ExtractableResponse<Response> response = httpPutWithAuthorization("/posts/" + postId + "/like", "");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("로그인한 회원은 좋아요 하지 않은 댓글에 좋아요를 할 수 있다.")
    @Test
    void flipLikeComment_Create() {
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getChrisToken());
        Long commentId = addCommentAndGetCommentId(1L);

        ExtractableResponse<Response> response = httpPutWithAuthorization("/comments/" + commentId + "/like",
                getJoshToken());
        LikeFlipResponse likeFlipResponse = response.jsonPath().getObject(".", LikeFlipResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("로그인한 회원이 좋아요를 누른 댓글에 좋아요를 취소할 수 있다.")
    @Test
    void flipLikeComment_Delete() {
        String chrisToken = getChrisToken();
        String joshToken = getJoshToken();
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, chrisToken);
        Long commentId = addCommentAndGetCommentId(1L);

        httpPutWithAuthorization("/comments/" + commentId + "/like", joshToken);
        ExtractableResponse<Response> response = httpPutWithAuthorization("/comments/" + commentId + "/like",
                joshToken);
        LikeFlipResponse likeFlipResponse = response.jsonPath().getObject(".", LikeFlipResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("로그인 하지 않은 회원이 댓글에 좋아요를 누를 경우 예외를 반환한다")
    @Test
    void flipLikeComment_Unauthorized() {
        String sessionId = getChrisToken();
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, sessionId);
        Long commentId = addCommentAndGetCommentId(1L);

        ExtractableResponse<Response> response = httpPutWithAuthorization("/comments/" + commentId + "/like", "");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private Long addCommentAndGetCommentId(Long postId) {
        return Long.parseLong(httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments", getChrisToken())
                .header("Location").split("/comments/")[1]);
    }
}
