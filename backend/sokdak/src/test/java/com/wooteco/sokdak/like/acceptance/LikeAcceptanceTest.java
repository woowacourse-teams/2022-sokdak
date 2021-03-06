package com.wooteco.sokdak.like.acceptance;

import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class LikeAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인한 회원은 좋아요 하지 않은 게시물에 좋아요를 할 수 있다.")
    @Test
    void flipLike_Create() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
                Collections.emptyList());
        httpPostWithAuthorization(newPostRequest, "/posts", getToken());

        ExtractableResponse<Response> response = httpPutWithAuthorization("/posts/1/like", getToken());
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
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
                Collections.emptyList());
        String sessionId = getToken();
        httpPostWithAuthorization(newPostRequest, "/posts", sessionId);

        httpPutWithAuthorization("/posts/1/like", sessionId);

        ExtractableResponse<Response> response = httpPutWithAuthorization("/posts/1/like", sessionId);
        LikeFlipResponse likeFlipResponse = response.jsonPath().getObject(".", LikeFlipResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("로그인 하지 않은 회원이 좋아요를 누를 경우 예외를 반환한다")
    @Test
    void flipLike_Unauthorized() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
                Collections.emptyList());
        String sessionId = getToken();
        httpPostWithAuthorization(newPostRequest, "/posts", sessionId);

        ExtractableResponse<Response> response = httpPutWithAuthorization("/posts/1/like", "");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
