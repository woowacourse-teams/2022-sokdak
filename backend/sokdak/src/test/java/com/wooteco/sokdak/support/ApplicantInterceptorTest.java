package com.wooteco.sokdak.support;

import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPatchWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getApplicantToken;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import com.wooteco.sokdak.util.fixture.BoardFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

class ApplicantInterceptorTest extends AcceptanceTest {

    @BeforeEach
    void setData() {
        addNewPost();
    }

    @DisplayName("지원자에게 허용된 GET 요청 테스트")
    @ParameterizedTest
    @CsvSource({
            "/notifications",
            "/notifications/check",
            "/members/nickname",
            "/refresh",
            "/logout",
            "/boards",
            "/boards/contents",
            "/posts/1/comments",
            "/posts?hashtag=태그1&size=5&page=0",
            "/hashtags/popular?include=태그&limit=3",
            "/members/signup/exists?username=testName",
            "/members/signup/exists?nickname=testNickname",
            "/members/nickname",
            "/notifications/check",
            "/notifications?size=2&page=1",
            "/posts/1",
            "/boards/2/posts",
            "/posts/me"
    })
    void preHandle_Get_True(String path) {
        String token = getApplicantToken();
        ExtractableResponse<Response> response = httpGetWithAuthorization(path, token);

        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("지원자에게 허용된 PATCH 요청 테스트")
    @ParameterizedTest
    @MethodSource("makeRequestBody_Patch")
    void preHandle_Patch_True(Object requestBody, String path) {
        String token = getApplicantToken();
        ExtractableResponse<Response> response = httpPatchWithAuthorization(requestBody, path, token);

        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.FORBIDDEN.value());
    }

    static Stream<Arguments> makeRequestBody_Patch() {
        return Stream.of(
                Arguments.of(new NicknameUpdateRequest("nickname"), "/members/nickname")
        );
    }

    @DisplayName("지원자에게 허용된 DELETE 요청 테스트")
    @ParameterizedTest
    @CsvSource({
            "/notifications/1"
    })
    void preHandle_Delete_True(String path) {
        String token = getApplicantToken();
        ExtractableResponse<Response> response = httpDeleteWithAuthorization(path, token);

        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("지원자에게 허용되지 않은 POST 요청 테스트")
    @ParameterizedTest
    @MethodSource("makeRequestBody_Post")
    void preHandle_Post_False(Object requestBody, String path) {
        String token = getApplicantToken();
        ExtractableResponse<Response> response = httpPostWithAuthorization(requestBody, path, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    static Stream<Arguments> makeRequestBody_Post() {
        return Stream.of(
                Arguments.of(new NewBoardRequest("name"), "/boards"),
                Arguments.of(new NewCommentRequest(FREE_BOARD_ID, "content", true), "/posts/1/comments"),
                Arguments.of(new NewReplyRequest("content", true), "/comments/1/reply"),
                Arguments.of(new NewPostRequest(FREE_BOARD_ID, "title", "content", true, new ArrayList<>()), "/boards/1/posts"),
                Arguments.of(new ReportRequest("message"), "/comments/1/report"),
                Arguments.of(new ReportRequest("message"), "/posts/1/report")
        );
    }

    @DisplayName("지원자에게 허용되지 않은 DELETE 요청 테스트")
    @ParameterizedTest
    @CsvSource({
            "/comments/1",
            "/posts/1"
    })
    void preHandle_Delete_False(String path) {
        String token = getApplicantToken();
        ExtractableResponse<Response> response = httpDeleteWithAuthorization(path, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("지원자에게 허용되지 않은 PUT 요청 테스트")
    @ParameterizedTest
    @MethodSource("makeRequestBody_Put")
    void preHandle_Put_False(Object requestBody, String path) {
        String token = getApplicantToken();
        ExtractableResponse<Response> response;
        if (Objects.isNull(requestBody)) {
            response = httpPutWithAuthorization(path, token);
        } else {
            response = httpPostWithAuthorization(requestBody, path, token);
        }

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    static Stream<Arguments> makeRequestBody_Put() {
        return Stream.of(
                Arguments.of(null, "/posts/1/like"),
                Arguments.of(null, "/comments/1/like"),
                Arguments.of(new PostUpdateRequest("title", "content", new ArrayList<>()), "/posts/1")
        );
    }
}
