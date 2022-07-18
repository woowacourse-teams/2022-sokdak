package com.wooteco.sokdak.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith(RestDocumentationExtension.class)
class PostControllerTest extends ControllerTest {

    private static final String SESSION_ID = "mySessionId";

    @Autowired
    PostController postController;

    @DisplayName("글 작성 요청을 받으면 새로운 게시글을 등록한다.")
    @Test
    void addPost() {
        NewPostRequest postRequest = new NewPostRequest("제목", "본문");
        given(postService.addPost(any()))
                .willReturn(1L);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("게시글 제목이 없는 경우 400을 반환한다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest postRequest = new NewPostRequest(null, "본문");

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("게시글 제목이 없는 경우 400을 반환한다.")
    @Test
    void addPost_Exception_NoContent() {
        NewPostRequest postRequest = new NewPostRequest("제목", null);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("게시글 목록 조회 요청을 받으면 해당되는 게시글들을 반환한다.")
    @Test
    void findPosts() {
        Post post1 = Post.builder()
                .title("제목1")
                .content("본문2")
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .build();
        given(postService.findPost(any()))
                .willReturn(PostResponse.from(post1), PostResponse.from(post2));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .when().get("/posts?size=3&page=0")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
    @Test
    void findPost() {
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .build();
        given(postService.findPost(any()))
                .willReturn(PostResponse.from(post));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .when().get("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 게시글에 대해 조회하면 404를 반환한다.")
    @Test
    void findPost_Exception_NoPost() {
        given(postService.findPost(any()))
                .willThrow(new PostNotFoundException());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .when().get("/posts/9999")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
