package com.wooteco.sokdak.post.acceptance;

import static com.wooteco.sokdak.util.HttpMethodFixture.getExceptionMessage;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpPutWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("게시글 관련 인수테스트")
@Sql("classpath:post_test.sql")
class PostAcceptanceTest extends AcceptanceTest {

    private static final NewPostRequest NEW_POST_REQUEST = new NewPostRequest("제목", "본문");

    @DisplayName("새로운 게시글을 작성할 수 있다.")
    @Test
    void addPost() {
        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getSessionId());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @DisplayName("로그인하지 않고, 게시물을 작성할 수 없다.")
    @Test
    void addPost_Unauthorized() {
        ExtractableResponse<Response> response = httpPost(NEW_POST_REQUEST, "/posts");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("게시글 목록 중 특정 페이지를 최신순으로 조회할 수 있다.")
    @Test
    void findPosts() {
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2");
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3");
        httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getSessionId());
        httpPostWithAuthorization(postRequest2, "/posts", getSessionId());
        httpPostWithAuthorization(postRequest3, "/posts", getSessionId());

        ExtractableResponse<Response> response = httpGet("/posts?size=2&page=0");
        List<String> postNames = parsePostTitles(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postNames).isEqualTo(List.of("제목3", "제목2"))
        );
    }

    @DisplayName("특정 게시글 조회할 수 있다.")
    @Test
    void findPost() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getSessionId()));

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostResponse postResponse = response.jsonPath().getObject(".", PostResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postResponse.getTitle()).isEqualTo(NEW_POST_REQUEST.getTitle()),
                () -> assertThat(postResponse.getContent()).isEqualTo(NEW_POST_REQUEST.getContent())
        );
    }

    @DisplayName("게시글 제목이 없는 경우 글 작성을 할 수 없다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest newPostRequestWithoutTitle = new NewPostRequest(null, "본문");
        ExtractableResponse<Response> response =
                httpPostWithAuthorization(newPostRequestWithoutTitle, "/posts", getSessionId());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(getExceptionMessage(response)).isEqualTo("제목은 1자 이상 50자 이하여야 합니다.")
        );
    }

    @DisplayName("존재하지 않는 게시글을 조회할 수 없다.")
    @Test
    void findPost_Exception_NoPost() {
        Long invalidPostId = 9999L;

        ExtractableResponse<Response> response = httpGet("/posts/" + invalidPostId);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(getExceptionMessage(response)).isEqualTo("게시물을 찾을 수 없습니다.")
        );
    }

    @DisplayName("게시글을 수정할 수 있다.")
    @Test
    void updatePost() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getSessionId()));

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("변경된 제목", "변경된 본문");
        ExtractableResponse<Response> response =
                httpPutWithAuthorization(postUpdateRequest, "/posts/" + postId, getSessionId());

        PostResponse postResponse = httpGet("/posts/" + postId).jsonPath().getObject(".", PostResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(postResponse.getTitle()).isEqualTo("변경된 제목"),
                () -> assertThat(postResponse.getContent()).isEqualTo("변경된 본문")
        );
    }

    @DisplayName("게시물을 삭제할 수 있다.")
    @Test
    void deletePost() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getSessionId()));

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/posts/" + postId, getSessionId());

        ExtractableResponse<Response> foundPostResponse = httpGet("/posts/" + postId);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(foundPostResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }

    private String getSessionId() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login")
                .cookie("JSESSIONID");
    }

    private String parsePostId(ExtractableResponse<Response> response) {
        return response.header("Location")
                .split("/posts/")[1];
    }

    private List<String> parsePostTitles(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", PostsResponse.class)
                .getPosts()
                .stream()
                .map(PostResponse::getTitle)
                .collect(Collectors.toList());
    }
}
