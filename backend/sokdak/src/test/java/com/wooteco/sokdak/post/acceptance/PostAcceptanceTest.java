package com.wooteco.sokdak.post.acceptance;

import static com.wooteco.sokdak.util.HttpMethodFixture.getExceptionMessage;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("게시글 관련 인수테스트")
class PostAcceptanceTest extends AcceptanceTest {

    @DisplayName("새로운 게시글을 작성할 수 있다.")
    @Test
    void addPost() {
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        ExtractableResponse<Response> response = httpPost(postRequest, "/posts");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @DisplayName("게시글 목록을 조회할 수 있다.")
    @Test
    void findPosts() {
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        httpPost(postRequest, "/posts");

        ExtractableResponse<Response> response = httpGet("/posts?size=3&page=0");
        PostsResponse postsResponse = response.jsonPath()
                .getObject(".", PostsResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postsResponse.getPosts()).hasSize(1)
        );
    }

    @DisplayName("특정 게시글 조회할 수 있다.")
    @Test
    void findPost() {
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        String postId = httpPost(postRequest, "/posts")
                .header("Location")
                .split("/posts/")[1];

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostResponse postResponse = response.jsonPath().getObject(".", PostResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postResponse.getTitle()).isEqualTo(postRequest.getTitle()),
                () -> assertThat(postResponse.getContent()).isEqualTo(postRequest.getContent())
        );
    }

    @DisplayName("게시글 제목이 없는 경우 글 작성을 할 수 없다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest postRequest = new NewPostRequest(null, "본문");
        ExtractableResponse<Response> response = httpPost(postRequest, "/posts");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(getExceptionMessage(response)).isEqualTo("제목 혹은 본문이 없습니다.")
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
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        String postId = httpPost(postRequest, "/posts")
                .header("Location")
                .split("/posts/")[1];

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("변경된 제목", "변경된 본문");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(postUpdateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/posts/" + postId)
                .then().log().all()
                .extract();

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
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        String postId = httpPost(postRequest, "/posts")
                .header("Location")
                .split("/posts/")[1];

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/posts/" + postId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> foundPostResponse = httpGet("/posts/" + postId);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(foundPostResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }
}
