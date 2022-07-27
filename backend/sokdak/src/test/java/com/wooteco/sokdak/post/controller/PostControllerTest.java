package com.wooteco.sokdak.post.controller;

import static com.wooteco.sokdak.post.util.PostFixture.UPDATED_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.UPDATED_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.hashtag.dto.HashtagResponse;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith(RestDocumentationExtension.class)
class PostControllerTest extends ControllerTest {

    private static final PostsElementResponse POSTS_ELEMENT_RESPONSE_1 = PostsElementResponse.builder()
            .id(1L)
            .title("제목1")
            .content("본문1")
            .createdAt(LocalDateTime.now())
            .likeCount(0)
            .commentCount(0)
            .modified(false)
            .build();
    private static final PostsElementResponse POSTS_ELEMENT_RESPONSE_2 = PostsElementResponse.builder()
            .id(2L)
            .title("제목2")
            .content("본문2")
            .createdAt(LocalDateTime.now())
            .likeCount(0)
            .commentCount(0)
            .modified(false)
            .build();

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("글 작성 요청을 받으면 새로운 게시글을 등록한다.")
    @Test
    void addPost() {
        NewPostRequest postRequest = new NewPostRequest("제목", "본문", List.of("태그1"));

        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("게시글 제목이 없는 경우 400을 반환한다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest postRequest = new NewPostRequest(null, "본문", Collections.emptyList());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("게시글 내용이 없는 경우 400을 반환한다.")
    @Test
    void addPost_Exception_NoContent() {
        NewPostRequest postRequest = new NewPostRequest("제목", null, Collections.emptyList());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("세션 정보를 가진 게시글 목록 조회 요청을 받으면 해당되는 게시글들을 반환한다.")
    @Test
    void findPosts() {
        doReturn(new PostsResponse(List.of(POSTS_ELEMENT_RESPONSE_1, POSTS_ELEMENT_RESPONSE_2), true))
                .when(postService)
                .findPosts(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/posts?size=3&page=0")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("세션 정보가 없는 게시글 목록 조회 요청을 받으면 해당되는 게시글들을 반환한다.")
    @Test
    void findPosts_NoSession() {
        doReturn(new PostsResponse(List.of(POSTS_ELEMENT_RESPONSE_1, POSTS_ELEMENT_RESPONSE_2), true))
                .when(postService)
                .findPosts(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/posts?size=3&page=0")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("세션 정보를 가진 특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
    @Test
    void findPost() {
        PostDetailResponse postResponse = PostDetailResponse.builder()
                .id(1L)
                .title("제목1")
                .content("본문1")
                .createdAt(LocalDateTime.now())
                .likeCount(0)
                .like(false)
                .modified(false)
                .hashtagResponses(List.of(new HashtagResponse(1L, "gogo")))
                .authorized(true)
                .build();
        doReturn(postResponse)
                .when(postService)
                .findPost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("세션 정보 없이 특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
    @Test
    void findPost_NoSession() {
        PostDetailResponse postResponse = PostDetailResponse.builder()
                .id(1L)
                .title("제목1")
                .content("본문1")
                .createdAt(LocalDateTime.now())
                .likeCount(0)
                .like(false)
                .modified(false)
                .hashtagResponses(List.of(new HashtagResponse(1L, "gogo")))
                .authorized(false)
                .build();
        doReturn(postResponse)
                .when(postService)
                .findPost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 게시글에 대해 조회하면 404를 반환한다.")
    @Test
    void findPost_Exception_NoPost() {
        doThrow(new PostNotFoundException())
                .when(postService)
                .findPost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().get("/posts/9999")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void updatePost() {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(UPDATED_POST_TITLE, UPDATED_POST_CONTENT,
                List.of("tag"));
        doNothing().when(postService)
                .updatePost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(postUpdateRequest)
                .when().put("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("권한이 없는 게시글을 수정하려고 하면 403을 반환한다.")
    @Test
    void updatePost_Exception_ForbiddenMemberId() {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(UPDATED_POST_TITLE, UPDATED_POST_CONTENT,
                List.of("tag"));
        doThrow(new AuthenticationException())
                .when(postService)
                .updatePost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(postUpdateRequest)
                .header("Authorization", "any")
                .when().put("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deletePost() {
        doNothing().when(postService)
                .deletePost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().delete("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("권한이 없는 게시글을 삭제하려고 하면 403을 반환한다.")
    @Test
    void deletePost_Exception_ForbiddenMemberId() {
        doThrow(new AuthenticationException())
                .when(postService)
                .deletePost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().delete("/posts/1")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
