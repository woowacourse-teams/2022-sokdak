package com.wooteco.sokdak.post.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getExceptionMessage;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getFiveTokens;
import static com.wooteco.sokdak.util.fixture.PostFixture.FREE_BOARD_POST_URI;
import static com.wooteco.sokdak.util.fixture.PostFixture.NEW_POST_REQUEST;
import static com.wooteco.sokdak.util.fixture.PostFixture.NEW_POST_REQUEST2;
import static com.wooteco.sokdak.util.fixture.PostFixture.UPDATED_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.UPDATED_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.post.dto.MyPostsResponse;
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

    @DisplayName("새로운 게시글을 작성할 수 있다.")
    @Test
    void addPost() {
        ExtractableResponse<Response> response = httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI,
                getChrisToken());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @DisplayName("로그인하지 않고, 게시물을 작성할 수 없다.")
    @Test
    void addPost_Unauthorized() {
        ExtractableResponse<Response> response = httpPost(NEW_POST_REQUEST, FREE_BOARD_POST_URI);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("게시글 목록 중 특정 페이지를 최신순으로 조회할 수 있다.")
    @Test
    void findPosts() {
        String token = getChrisToken();
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", false, Collections.emptyList());
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", false, Collections.emptyList());
        httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI, token);
        httpPostWithAuthorization(postRequest2, FREE_BOARD_POST_URI, token);
        httpPostWithAuthorization(postRequest3, FREE_BOARD_POST_URI, token);
        Long writableBoardID = 2L;

        ExtractableResponse<Response> response = httpGet("/boards/" + writableBoardID + "/posts?size=2&page=0");
        List<String> postNames = parsePostTitles(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postNames).isEqualTo(List.of("제목3", "제목2"))
        );
    }

    @DisplayName("특정 게시판에 글을 쓰고 그 게시판에서 글 조회가 가능하다.")
    @Test
    void findPostsInBoard() {
        String token = getChrisToken();
        NewPostRequest postRequest1 = new NewPostRequest("제목1", "본문1", false, Collections.emptyList());
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", false, Collections.emptyList());
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", false, Collections.emptyList());
        httpPostWithAuthorization(postRequest1, FREE_BOARD_POST_URI, token);
        httpPostWithAuthorization(postRequest2, FREE_BOARD_POST_URI, token);
        httpPostWithAuthorization(postRequest3, "/boards/3/posts", token);

        ExtractableResponse<Response> response = httpGet("/boards/3/posts?size=2&page=0");
        List<String> postNames = parsePostTitles(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postNames).isEqualTo(List.of("제목3"))
        );
    }

    @DisplayName("특정 게시판의 게시글 목록을 조회할때 누적신고 5개 이상인 게시글은 block된다.")
    @Test
    void findPosts_Block() {
        Long blockedPostId = addNewPost();
        addNewPost();
        List<String> tokens = getFiveTokens();
        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("신고");
            httpPostWithAuthorization(reportRequest, "/posts/" + blockedPostId + "/report", tokens.get(i));
        }

        ExtractableResponse<Response> response = httpGet(FREE_BOARD_POST_URI + "?size=2&page=0");
        List<PostsElementResponse> postsElementResponses = response
                .jsonPath()
                .getObject(".", PostsResponse.class)
                .getPosts();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postsElementResponses.get(0).isBlocked()).isFalse(),
                () -> assertThat(postsElementResponses.get(1).isBlocked()).isTrue(),
                () -> assertThat(postsElementResponses.get(1).getTitle()).isEqualTo("블라인드 처리된 글입니다"),
                () -> assertThat(postsElementResponses.get(1).getContent()).isEqualTo("블라인드 처리된 글입니다")
        );
    }

    @DisplayName("특정 게시글을 조회할때 누적신고 5개 이상인 게시글은 block된다.")
    @Test
    void findPost_Block() {
        Long blockedPostId = addNewPost();
        List<String> tokens = getFiveTokens();
        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("신고");
            httpPostWithAuthorization(reportRequest, "/posts/" + blockedPostId + "/report", tokens.get(i));
        }

        ExtractableResponse<Response> response = httpGet(
                "/posts/" + blockedPostId);
        PostDetailResponse postDetailResponse = response
                .jsonPath()
                .getObject(".", PostDetailResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.isBlocked()).isTrue(),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo("블라인드 처리된 글입니다"),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo("블라인드 처리된 글입니다")
        );
    }

    @DisplayName("회원이 글을 작성하지 못하는 게시판에 글을 쓰면 예외를 반환한다.")
    @Test
    void findPostsInBoard_Exception() {
        String token = getChrisToken();
        String invalidCreatePostUri = "/boards/1/posts";

        ExtractableResponse<Response> response =
                httpPostWithAuthorization(NEW_POST_REQUEST, invalidCreatePostUri, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("기명으로 작성된 게시글 조회할 수 있다.")
    @Test
    void findPost_IdentifiedNickname() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI, getChrisToken()));

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo(NEW_POST_REQUEST.getTitle()),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo(NEW_POST_REQUEST.getContent()),
                () -> assertThat(postDetailResponse.getNickname()).isEqualTo("chrisNickname"),
                () -> assertThat(postDetailResponse.getBoardId()).isEqualTo(2)
        );
    }

    @DisplayName("익명으로 작성된 게시글 조회할 수 있다.")
    @Test
    void findPost_Anonymous() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT, true,
                Collections.emptyList());
        String postId = parsePostId(
                httpPostWithAuthorization(newPostRequest, FREE_BOARD_POST_URI, getChrisToken()));

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo(NEW_POST_REQUEST.getTitle()),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo(NEW_POST_REQUEST.getContent()),
                () -> assertThat(postDetailResponse.getNickname()).isNotEqualTo("chrisNickname"),
                () -> assertThat(postDetailResponse.getBoardId()).isEqualTo(2)
        );
    }

    @DisplayName("게시글 제목이 없는 경우 글 작성을 할 수 없다.")
    @Test
    void addPost_Exception_NoTitle() {
        NewPostRequest newPostRequestWithoutTitle = new NewPostRequest(null, VALID_POST_CONTENT,
                false, Collections.emptyList());
        ExtractableResponse<Response> response =
                httpPostWithAuthorization(newPostRequestWithoutTitle, FREE_BOARD_POST_URI, getChrisToken());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(getExceptionMessage(response)).isEqualTo("제목은 1자 이상 50자 이하여야 합니다.")
        );
    }

    @DisplayName("존재하지 않는 게시글을 조회할 수 없다.")
    @Test
    void findPost_Exception_NoPost() {
        long invalidPostId = 9999L;

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
                httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI, getChrisToken()));

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                UPDATED_POST_TITLE, UPDATED_POST_CONTENT, Collections.emptyList());
        ExtractableResponse<Response> response =
                httpPutWithAuthorization(postUpdateRequest, "/posts/" + postId, getChrisToken());

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
                httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI, getChrisToken()));

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/posts/" + postId, getChrisToken());

        ExtractableResponse<Response> foundPostResponse = httpGet("/posts/" + postId);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(foundPostResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }

    @DisplayName("내가 쓴 글을 최신순으로 볼 수 있다.")
    @Test
    void searchMyPosts() {
        String token = getChrisToken();
        httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI, token);
        httpPostWithAuthorization(NEW_POST_REQUEST2, FREE_BOARD_POST_URI, token);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/posts/me?size=3&page=0", token);
        List<PostsElementResponse> myPostsResponse = toMyPostsResponse(response).getPosts();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(myPostsResponse.get(0).getTitle()).isEqualTo("제목2"),
                () -> assertThat(myPostsResponse.get(0).getContent()).isEqualTo("본문2"),
                () -> assertThat(myPostsResponse.get(1).getTitle()).isEqualTo("제목"),
                () -> assertThat(myPostsResponse.get(1).getContent()).isEqualTo("본문")
        );
    }

    @DisplayName("내가 쓴 글을 조회 시 잘못된 페이지로 접근하면 0개의 글이 조회되고 1개의 페이지가 카운트된다")
    @Test
    void searchMyPosts_Exception_NoPage() {
        String token = getChrisToken();
        httpPostWithAuthorization(NEW_POST_REQUEST, FREE_BOARD_POST_URI, token);
        httpPostWithAuthorization(NEW_POST_REQUEST2, FREE_BOARD_POST_URI, token);
        long wrongPage = 5L;

        ExtractableResponse<Response> response = httpGetWithAuthorization("/posts/me?size=3&page=" + wrongPage, token);
        MyPostsResponse postsResponse = toMyPostsResponse(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postsResponse.getPosts()).isEmpty(),
                () -> assertThat(postsResponse.getTotalPageCount()).isEqualTo(1)
        );
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

    private MyPostsResponse toMyPostsResponse(ExtractableResponse<Response> response) {
        return response.body()
                .jsonPath()
                .getObject(".", MyPostsResponse.class);
    }
}
