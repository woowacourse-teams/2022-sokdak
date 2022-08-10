package com.wooteco.sokdak.notification.acceptance;

import static com.wooteco.sokdak.util.fixture.CommentFixture.NEW_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.addCommentAndGetCommentId;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getTokensForLike;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getTokensForReport;
import static com.wooteco.sokdak.util.fixture.PostFixture.addPostAndGetPostId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("알림 관련 테스트")
class NotificationAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글에 댓글이 등록되면 게시글 사용자에게 알림이 등록된다.")
    @Test
    void checkNewNotification_NewComment() {
        Long postId = addPostAndGetPostId();
        LoginRequest loginRequest = new LoginRequest("josh", "Abcd123!@");
        String token = httpPost(loginRequest, "/login").header(AUTHORIZATION);
        httpPostWithAuthorization(NEW_COMMENT_REQUEST, "/posts/" + postId + "/comments", token);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", getToken());
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isTrue()
        );
    }

    @DisplayName("게시글이 HOT 게시판에 등록되면 게시글 사용자에게 알림이 등록된다.")
    @Test
    void checkNewNotification_PostInHotBoard() {
        addPostAndGetPostId();
        List<String> otherTokens = getTokensForLike();
        for (String token : otherTokens) {
            httpPutWithAuthorization("/posts/1/like", token);
        }

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", getToken());
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isTrue()
        );
    }

    @DisplayName("게시글이 5회 신고 되면 게시글 작성자에게 알림이 등록된다.")
    @Test
    void checkNewNotification_PostReport() {
        Long postId = addPostAndGetPostId();
        List<String> reporterTokens = getTokensForReport();
        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("신고");
            httpPostWithAuthorization(reportRequest, "/posts/" + postId + "/report", reporterTokens.get(i));
        }

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", getToken());
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isTrue()
        );
    }

    @DisplayName("댓글이 5회 신고 되면 댓글 작성자에게 알림이 등록된다.")
    @Test
    void checkNewNotification_CommentReport() {
        Long postId = addPostAndGetPostId();
        String commenterToken = getToken();
        httpPostWithAuthorization(NEW_COMMENT_REQUEST, "/posts/" + postId + "/comments", commenterToken);
        Long commentId = addCommentAndGetCommentId(postId);
        List<String> reporterTokens = getTokensForReport();
        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("댓글신고");
            httpPostWithAuthorization(reportRequest, "/comments/" + commentId + "/report", reporterTokens.get(i));
        }

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", commenterToken);
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isTrue()
        );
    }
}