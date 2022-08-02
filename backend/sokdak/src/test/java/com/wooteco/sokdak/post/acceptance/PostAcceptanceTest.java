package com.wooteco.sokdak.post.acceptance;

import static com.wooteco.sokdak.post.util.PostFixture.UPDATED_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.UPDATED_POST_TITLE;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getExceptionMessage;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.PostFixture.CANNOT_CREATE_POST_URI;
import static com.wooteco.sokdak.util.fixture.PostFixture.CREATE_POST_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("게시글 관련 인수테스트")
class PostAcceptanceTest extends AcceptanceTest {

    private static final NewPostRequest NEW_POST_REQUEST = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
            Collections.emptyList());
    public static final long WRITABLE_BOARD_ID = 2L;

    @DisplayName("새로운 게시글을 작성할 수 있다.")
    @Test
    void addPost() {
        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI,
                getToken());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @DisplayName("로그인하지 않고, 게시물을 작성할 수 없다.")
    @Test
    void addPost_Unauthorized() {
        ExtractableResponse<Response> response = httpPost(NEW_POST_REQUEST, CREATE_POST_URI);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("게시글 목록 중 특정 페이지를 최신순으로 조회할 수 있다.")
    @Test
    void findPosts() {
        String token = getToken();
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", Collections.emptyList());
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", Collections.emptyList());
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, token);
        httpPostWithAuthorization(postRequest2, CREATE_POST_URI, token);
        httpPostWithAuthorization(postRequest3, CREATE_POST_URI, token);

        ExtractableResponse<Response> response = httpGet("/boards/" + WRITABLE_BOARD_ID + "/posts?size=2&page=0");
        List<String> postNames = parsePostTitles(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postNames).isEqualTo(List.of("제목3", "제목2"))
        );
    }

    @DisplayName("게시글 목록 중 누적 신고가 5개 이상인 게시글은 blocked 왼다.")
    @Test
    void findPosts_Blocked() {
        String token = getToken();
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", Collections.emptyList());
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, token);
        Long blockedPostId = Long.parseLong(
                parsePostId(httpPostWithAuthorization(postRequest2, CREATE_POST_URI, token)));
        reportPostToBlockPost(blockedPostId);

        ExtractableResponse<Response> response = httpGet("/boards/1/posts?size=2&page=0");
        List<PostsElementResponse> postsElementResponses = parsePostsElementResponse(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postsElementResponses.get(0).isBlocked()).isTrue(),
                () -> assertThat(postsElementResponses.get(1).isBlocked()).isFalse()
        );
    }

    @DisplayName("특정 게시판에 글을 쓰고 그 게시판에서 글 조회가 가능하다.")
    @Test
    void findPostsInBoard() {
        String token = getToken();
        NewPostRequest postRequest1 = new NewPostRequest("제목1", "본문1", Collections.emptyList());
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", Collections.emptyList());
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", Collections.emptyList());
        httpPostWithAuthorization(postRequest1, CREATE_POST_URI, token);
        httpPostWithAuthorization(postRequest2, CREATE_POST_URI, token);
        httpPostWithAuthorization(postRequest3, "/boards/3/posts", token);

        ExtractableResponse<Response> response = httpGet("/boards/3/posts?size=2&page=0");
        List<String> postNames = parsePostTitles(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postNames).isEqualTo(List.of("제목3"))
        );
    }

    @DisplayName("회원이 글을 작성하지 못하는 게시판에 글을 쓰면 예외를 반환한다.")
    @Test
    void findPostsInBoard_Exception() {
        String token = getToken();
        NewPostRequest postRequest1 = new NewPostRequest("제목1", "본문1", Collections.emptyList());
        ExtractableResponse<Response> response = httpPostWithAuthorization(postRequest1, CANNOT_CREATE_POST_URI,
                token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("특정 게시글 조회할 수 있다.")
    @Test
    void findPost() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getToken()));

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo(NEW_POST_REQUEST.getTitle()),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo(NEW_POST_REQUEST.getContent())
        );
    }

    @DisplayName("누적신고가 5개 이상인 글은 blocked 된다.")
    @Test
    void findPost_Blocked() {
        Long postId = Long.parseLong(
                parsePostId(httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getToken())));
        reportPostToBlockPost(postId);

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.isBlocked()).isTrue()
        );
    }

    @DisplayName("게시글 제목이 없는 경우 글 작성을 할 수 없다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest newPostRequestWithoutTitle = new NewPostRequest(null, VALID_POST_CONTENT,
                Collections.emptyList());
        ExtractableResponse<Response> response =
                httpPostWithAuthorization(newPostRequestWithoutTitle, CREATE_POST_URI, getToken());

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
                httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getToken()));

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                UPDATED_POST_TITLE, UPDATED_POST_CONTENT, Collections.emptyList());
        ExtractableResponse<Response> response =
                httpPutWithAuthorization(postUpdateRequest, "/posts/" + postId, getToken());

        PostDetailResponse postDetailResponse = httpGet("/posts/" + postId).jsonPath()
                .getObject(".", PostDetailResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo(UPDATED_POST_TITLE),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo(UPDATED_POST_CONTENT)
        );
    }

    @DisplayName("게시물을 삭제할 수 있다.")
    @Test
    void deletePost() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, getToken()));

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/posts/" + postId, getToken());

        ExtractableResponse<Response> foundPostResponse = httpGet("/posts/" + postId);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(foundPostResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
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
                .map(PostsElementResponse::getTitle)
                .collect(Collectors.toList());
    }

    private List<PostsElementResponse> parsePostsElementResponse(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", PostsResponse.class)
                .getPosts();
    }

    private void reportPostToBlockPost(Long postId) {
        String token1 = getToken();
        String token2 = httpPost(new LoginRequest("josh", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token3 = httpPost(new LoginRequest("thor", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token4 = httpPost(new LoginRequest("hunch", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token5 = httpPost(new LoginRequest("east", "Abcd123!@"), "/login").header(AUTHORIZATION);
        List<String> tokens = List.of(token1, token2, token3, token4, token5);

        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("글신고");
            httpPostWithAuthorization(reportRequest, "/posts/" + postId + "/report", tokens.get(i));
        }
    }
}
