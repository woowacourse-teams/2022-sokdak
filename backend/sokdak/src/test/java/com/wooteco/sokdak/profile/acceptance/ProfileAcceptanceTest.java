package com.wooteco.sokdak.profile.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPatchWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.PostFixture.CREATE_POST_URI;
import static com.wooteco.sokdak.util.fixture.PostFixture.NEW_POST_REQUEST;
import static com.wooteco.sokdak.util.fixture.PostFixture.NEW_POST_REQUEST2;
import static com.wooteco.sokdak.util.fixture.PostFixture.POSTS_ELEMENT_RESPONSE_1;
import static com.wooteco.sokdak.util.fixture.PostFixture.POSTS_ELEMENT_RESPONSE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.profile.dto.MyPostsResponse;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProfileAcceptanceTest extends AcceptanceTest {

    private static final String WRONG_PAGE = "5";

    @DisplayName("닉네임을 수정할 수 있다.")
    @Test
    void editNickname() {
        String nickname = "chrisNick2";
        NicknameUpdateRequest nicknameRequest = new NicknameUpdateRequest(nickname);
        String token = getToken();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(nicknameRequest,
                "/members/nickname", token);
        NicknameResponse nicknameResponse = toNicknameResponse(httpGetWithAuthorization("/members/nickname", token));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo(nickname)
        );
    }

    @DisplayName("중복된 닉네임으로 수정할 수 없다.")
    @Test
    void editNickname_Exception_Duplicate() {
        NicknameUpdateRequest wrongNicknameRequest = new NicknameUpdateRequest("eastNickname");
        String token = getToken();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(wrongNicknameRequest,
                "/members/nickname", token);
        NicknameResponse nicknameResponse = toNicknameResponse(httpGetWithAuthorization("/members/nickname", token));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo("chrisNickname")
        );
    }

    @DisplayName("내가 쓴 글을 볼 수 있다.")
    @Test
    void searchMyPosts() {
        String token = getToken();
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, token);
        httpPostWithAuthorization(NEW_POST_REQUEST2, CREATE_POST_URI, token);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/posts/me?size=3&page=0", token);
        MyPostsResponse myPostsResponse = toMyPostsResponse(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(myPostsResponse.getPosts().size()).isEqualTo(2),
                () -> assertThat(myPostsResponse.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(POSTS_ELEMENT_RESPONSE_2, POSTS_ELEMENT_RESPONSE_1))
        );
    }

    @DisplayName("내가 쓴 글을 조회 시 잘못된 페이지로 접근하면 0개의 글이 조회되고 1개의 페이지가 카운트된다")
    @Test
    void searchMyPosts_Exception_NoPage() {
        String token = getToken();
        httpPostWithAuthorization(NEW_POST_REQUEST, CREATE_POST_URI, token);
        httpPostWithAuthorization(NEW_POST_REQUEST2, CREATE_POST_URI, token);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/posts/me?size=3&page=" + WRONG_PAGE, token);
        MyPostsResponse postsResponse = toMyPostsResponse(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(postsResponse.getPosts()).isEmpty(),
                () -> assertThat(postsResponse.getTotalPageCount()).isEqualTo(1)
        );
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    private NicknameResponse toNicknameResponse(ExtractableResponse<Response> getWithAuthorization) {
        return getWithAuthorization.body()
                .jsonPath()
                .getObject(".", NicknameResponse.class);
    }

    private MyPostsResponse toMyPostsResponse(ExtractableResponse<Response> response) {
        return response.body()
                .jsonPath()
                .getObject(".", MyPostsResponse.class);
    }
}
