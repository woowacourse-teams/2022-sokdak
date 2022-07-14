package com.wooteco.sokdak.post.controller;

import static com.wooteco.sokdak.util.HttpMethodFixture.getExceptionMessage;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.util.AcceptanceTest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class PostControllerTest extends AcceptanceTest {

    @Autowired
    PostController postController;

    @DisplayName("글 작성 요청을 받으면 새로운 게시글을 등록한다.")
    @Test
    void addPost() {
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        ExtractableResponse<Response> response = httpPost(postRequest, "/posts");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @DisplayName("게시글 목록 조회 요청을 받으면 해당되는 게시글들을 반환한다.")
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

    @DisplayName("특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
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

    @DisplayName("게시글 제목이 없는 경우 400을 반환합니다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest postRequest = new NewPostRequest(null, "본문");
        ExtractableResponse<Response> response = httpPost(postRequest, "/posts");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(getExceptionMessage(response)).isEqualTo("제목 혹은 본문이 없습니다.")
        );
    }

    @DisplayName("존재하지 않는 게시글에 대해 조회하면 404를 반환합니다.")
    @Test
    void findPost_Exception_NoPost() {
        Long invalidPostId = 9999L;

        ExtractableResponse<Response> response = httpGet("/posts/" + invalidPostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
