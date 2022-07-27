package com.wooteco.sokdak.hashtag.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.dto.HashtagResponse;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("해시태그 관련 인수테스트")
public class HashtagAcceptanceTest extends AcceptanceTest {

    @Autowired
    private HashtagRepository hashtagRepository;

    private static final NewPostRequest NEW_POST_REQUEST = new NewPostRequest("제목", "본문", List.of("태그1", "태그2"));

    @DisplayName("게시글을 작성하면서 해시태그를 추가하면, 게시글이 조회될 때 해시태그도 조회된다.")
    @Test
    void addPostWithHashtags() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getToken()));

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getHashtags())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(new HashtagResponse(1L, "태그1"), new HashtagResponse(2L, "태그2")))
        );
    }

    @DisplayName("해시태그가 포함된 게시글을 작성하고, 게시글의 해시태그를 수정하면, 게시글을 조회 했을 때 수정된 해시태그를 조회할 수 있다.")
    @Test
    void modifyPostWithHashtags() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getToken()));

        final PostUpdateRequest requestBody = new PostUpdateRequest("제목", "본문", List.of("변경1", "변경2"));
        httpPutWithAuthorization(requestBody, "/posts/" + postId, getToken());

        ExtractableResponse<Response> response = httpGet("/posts/" + postId);
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postDetailResponse.getHashtags())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(new HashtagResponse(1L, "변경1"), new HashtagResponse(2L, "변경2")))
        );
    }

    @DisplayName("해시태그가 포함된 게시글이 정상적으로 삭제된다.")
    @Test
    void deletePostWithHashtags() {
        String postId = parsePostId(
                httpPostWithAuthorization(NEW_POST_REQUEST, "/posts", getToken()));

        ExtractableResponse<Response> response = httpDeleteWithAuthorization("/posts/" + postId, getToken());


        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("해시태그로 검색하면 해당 해시태그가 포함된 게시물들을 조회한다.")
    @Test
    void searchWithHashtag() {
        NewPostRequest postRequest1 = new NewPostRequest("제목1", "본문1", List.of("태그1"));
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", List.of("태그2"));
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", List.of("태그1","태그2"));

        httpPostWithAuthorization(postRequest1, "/posts", getToken());
        httpPostWithAuthorization(postRequest2, "/posts", getToken());
        httpPostWithAuthorization(postRequest3, "/posts", getToken());

        ExtractableResponse<Response> response = httpGet("/posts?hashtag=태그1&size=10&page=0");
        List<String> postNames = parsePostTitles(response);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postNames).containsExactly("제목3","제목1")
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
}
