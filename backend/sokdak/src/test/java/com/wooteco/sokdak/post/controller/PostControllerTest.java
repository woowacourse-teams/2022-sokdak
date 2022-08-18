package com.wooteco.sokdak.post.controller;

import static com.wooteco.sokdak.util.fixture.PostFixture.UPDATED_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.UPDATED_POST_TITLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.hashtag.dto.HashtagResponse;
import com.wooteco.sokdak.post.dto.MyPostsResponse;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith(RestDocumentationExtension.class)
class PostControllerTest extends ControllerTest {

    private static final NewPostRequest NEW_POST_REQUEST =
            new NewPostRequest("제목", "본문", false, List.of("태그1", "태그2"));
    private static final int WRONG_PAGE = 99;
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

    @DisplayName("글 작성 요청을 받으면 새로운 게시글을 등록한다.")
    @Test
    void addPost() {
        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(NEW_POST_REQUEST)
                .when().post("/boards/1/posts")
                .then().log().all()
                .apply(document("post/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("게시글 제목이 없는 경우 400을 반환한다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest postRequest = new NewPostRequest(null, "본문", false, Collections.emptyList());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(postRequest)
                .when().post("/boards/1/posts")
                .then().log().all()
                .apply(document("post/create/fail/noTitle"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("게시글 내용이 없는 경우 400을 반환한다.")
    @Test
    void addPost_Exception_NoContent() {
        NewPostRequest postRequest = new NewPostRequest("제목", null, false, Collections.emptyList());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(postRequest)
                .when().post("/boards/1/posts")
                .then().log().all()
                .apply(document("post/create/fail/noContent"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("인증된 상태로 게시글 목록 조회 요청을 받으면 해당되는 게시글들을 반환한다.")
    @Test
    void findPosts_Authorized() {
        doReturn(new PostsResponse(List.of(POSTS_ELEMENT_RESPONSE_1, POSTS_ELEMENT_RESPONSE_2), true))
                .when(postService)
                .findPostsByBoard(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().get("/boards/1/posts?size=3&page=0")
                .then().log().all()
                .apply(document("post/find/all/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
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
                .apply(document("post/find/one/success"))
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
                .apply(document("post/find/one/fail"))
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
                .apply(document("post/update/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("권한이 없는 게시글을 수정하려고 하면 403을 반환한다.")
    @Test
    void updatePost_Exception_ForbiddenMemberId() {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(UPDATED_POST_TITLE, UPDATED_POST_CONTENT,
                List.of("tag"));
        doThrow(new AuthorizationException())
                .when(postService)
                .updatePost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(postUpdateRequest)
                .header("Authorization", "any")
                .when().put("/posts/1")
                .then().log().all()
                .apply(document("post/update/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("게시물 수정시 제목 혹은 본문에 내용이 없는 경우 400을 반환한다.")
    @Test
    void updatePost_Exception_NoContentTitle() {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(null, UPDATED_POST_CONTENT,
                List.of("tag"));
        doThrow(new AuthorizationException())
                .when(postService)
                .updatePost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(postUpdateRequest)
                .header("Authorization", "any")
                .when().put("/posts/1")
                .then().log().all()
                .apply(document("post/update/fail/noContent"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
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
                .apply(document("post/delete/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("권한이 없는 게시글을 삭제하려고 하면 403을 반환한다.")
    @Test
    void deletePost_Exception_ForbiddenMemberId() {
        doThrow(new AuthorizationException())
                .when(postService)
                .deletePost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().delete("/posts/1")
                .then().log().all()
                .apply(document("post/delete/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("내가 쓴 글 조회 시 200 반환")
    @Test
    void findMyPosts() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        MyPostsResponse myPostsResponse = new MyPostsResponse(
                List.of(POSTS_ELEMENT_RESPONSE_2, POSTS_ELEMENT_RESPONSE_1),
                5);
        doReturn(myPostsResponse)
                .when(postService)
                .findMyPosts(refEq(pageRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer any")
                .when().get("/posts/me?size=2&page=0")
                .then().log().all()
                .assertThat()
                .apply(document("member/find/posts/success/postIn"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("내가 쓴 글의 없는 페이지 조회 시 200 반환")
    @Test
    void findMyPosts_Exception_NoPage() {
        PageRequest pageRequest = PageRequest.of(WRONG_PAGE, 2);
        doReturn(new MyPostsResponse(Collections.emptyList(), 5))
                .when(postService)
                .findMyPosts(refEq(pageRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer any")
                .when().get("/posts/me?size=2&page=" + WRONG_PAGE)
                .then().log().all()
                .assertThat()
                .apply(document("member/find/posts/success/noPost"))
                .statusCode(HttpStatus.OK.value());
    }
}
