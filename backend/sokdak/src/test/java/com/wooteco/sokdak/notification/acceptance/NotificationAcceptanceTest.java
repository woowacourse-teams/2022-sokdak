package com.wooteco.sokdak.notification.acceptance;

import static com.wooteco.sokdak.util.fixture.CommentFixture.NON_ANONYMOUS_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.NON_ANONYMOUS_REPLY_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.addNewCommentInPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static com.wooteco.sokdak.util.fixture.TokenFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.TokenFixture.getToken;
import static com.wooteco.sokdak.util.fixture.TokenFixture.getTokens;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
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
        Long postId = addNewPost();
        LoginRequest loginRequest = new LoginRequest("josh", "Abcd123!@");
        String token = httpPost(loginRequest, "/login").header(AUTHORIZATION);
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", token);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", getChrisToken());
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isTrue()
        );
    }

    @DisplayName("자신의 게시글에 댓글을 달면 알림이 등록되지 않는다.")
    @Test
    void checkNewNotification_NewComment_MyPost() {
        Long postId = addNewPost();
        String token = getChrisToken();
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", token);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", token);
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isFalse()
        );
    }

    @DisplayName("자신의 댓글에 대댓글이 달리면 댓글 작성자에게 알림이 등록된다.")
    @Test
    void checkNewNotification_NewReply() {
        Long postId = addNewPost();
        String chrisToken = getChrisToken();
        String joshToken = getToken("josh");
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", joshToken);
        httpPostWithAuthorization(NON_ANONYMOUS_REPLY_REQUEST, "/comments" + 1 + "/reply", chrisToken);

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", joshToken);
        NewNotificationCheckResponse newNotificationCheckResponse =
                response.jsonPath().getObject(".", NewNotificationCheckResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isFalse()
        );
    }

    @DisplayName("게시글이 HOT 게시판에 등록되면 게시글 사용자에게 알림이 등록된다.")
    @Test
    void checkNewNotification_PostInHotBoard() {
        addNewPost();
        List<String> otherTokens = getTokens();
        for (String token : otherTokens) {
            httpPutWithAuthorization("/posts/1/like", token);
        }

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", getChrisToken());
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
        Long postId = addNewPost();
        List<String> reporterTokens = getTokens();
        for (int i = 0; i < 5; ++i) {
            ReportRequest reportRequest = new ReportRequest("신고");
            httpPostWithAuthorization(reportRequest, "/posts/" + postId + "/report", reporterTokens.get(i));
        }

        ExtractableResponse<Response> response = httpGetWithAuthorization("/notifications/check", getChrisToken());
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
        Long postId = addNewPost();
        String commenterToken = getChrisToken();
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", commenterToken);
        Long commentId = addNewCommentInPost(postId);
        List<String> reporterTokens = getTokens();
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

    @DisplayName("알림 목록을 조회할 수 있다.")
    @Test
    void findNotifications() {
        Long postId = addNewPost();
        String token = getChrisToken();

        String joshToken = getToken("josh");
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", joshToken);

        List<String> otherTokens = getTokens();
        for (String other : otherTokens) {
            httpPutWithAuthorization("/posts/1/like", other);
        }

        ExtractableResponse<Response> response =
                httpGetWithAuthorization("/notifications?size=2&page=0", token);
        NotificationsResponse notificationsResponse = response.jsonPath()
                .getObject(".", NotificationsResponse.class);
        NewNotificationCheckResponse newNotificationCheckResponse =
                httpGetWithAuthorization("/notifications/check", token)
                        .jsonPath()
                        .getObject(".", NewNotificationCheckResponse.class);

        NotificationResponse notificationResponse1 = notificationsResponse.getNotifications().get(0);
        NotificationResponse notificationResponse2 = notificationsResponse.getNotifications().get(1);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(notificationsResponse.isLastPage()).isTrue(),
                () -> assertThat(notificationResponse1.getPostId()).isEqualTo(postId),
                () -> assertThat(notificationResponse1.getType()).isEqualTo("HOT_BOARD"),
                () -> assertThat(notificationResponse1.getContent()).isEqualTo(VALID_POST_TITLE),
                () -> assertThat(notificationResponse2.getPostId()).isEqualTo(postId),
                () -> assertThat(notificationResponse2.getType()).isEqualTo("NEW_COMMENT"),
                () -> assertThat(notificationResponse2.getContent()).isEqualTo(VALID_POST_TITLE),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isFalse()
        );
    }

    @DisplayName("알림을 삭제할 수 있다.")
    @Test
    void deleteNotification() {
        Long postId = addNewPost();
        LoginRequest loginRequest = new LoginRequest("josh", "Abcd123!@");
        String token = httpPost(loginRequest, "/login").header(AUTHORIZATION);
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", token);

        ExtractableResponse<Response> response =
                httpDeleteWithAuthorization("/notifications/1", getChrisToken());

        NewNotificationCheckResponse newNotificationCheckResponseAfterDeletion =
                httpGetWithAuthorization("/notifications/check", token)
                        .jsonPath()
                        .getObject(".", NewNotificationCheckResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(newNotificationCheckResponseAfterDeletion.isExistence()).isFalse()
        );
    }


    @DisplayName("게시글이 삭제되면 게시글에 대한 알림들이 모두 삭제된다.")
    @Test
    void deletePost_deleteNotificationByPostId() {
        Long postId = addNewPost();
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", getToken("josh"));
        NotificationsResponse notificationsResponseBeforePostDeletion =
                httpGetWithAuthorization("/notifications?size=2&page=0", getChrisToken())
                        .jsonPath()
                        .getObject(".", NotificationsResponse.class);

        httpDeleteWithAuthorization("/posts/" + postId, getChrisToken());

        NotificationsResponse notificationsResponseAfterPostDeletion =
                httpGetWithAuthorization("/notifications?size=2&page=0", getChrisToken())
                        .jsonPath()
                        .getObject(".", NotificationsResponse.class);
        assertAll(
                () -> assertThat(notificationsResponseBeforePostDeletion.getNotifications()).hasSize(1),
                () -> assertThat(notificationsResponseAfterPostDeletion.getNotifications()).isEmpty()
        );
    }

    @DisplayName("댓글이 삭제되면 댓글에 대한 알림들이 모두 삭제된다.")
    @Test
    void deleteComment_deleteNotificationByCommentId() {
        Long postId = addNewPost();
        httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments", getToken("josh"));
        httpPostWithAuthorization(NON_ANONYMOUS_REPLY_REQUEST, "/comments/" + 1 + "/reply", getChrisToken());
        NotificationsResponse notificationsResponseBeforeCommentDeletion =
                httpGetWithAuthorization("/notifications?size=2&page=0", getToken("josh"))
                        .jsonPath()
                        .getObject(".", NotificationsResponse.class);

        httpDeleteWithAuthorization("/comments/1", getToken("josh"));

        NotificationsResponse notificationsResponseAfterCommentDeletion =
                httpGetWithAuthorization("/notifications?size=2&page=0", getToken("josh"))
                        .jsonPath()
                        .getObject(".", NotificationsResponse.class);
        assertAll(
                () -> assertThat(notificationsResponseBeforeCommentDeletion.getNotifications()).hasSize(1),
                () -> assertThat(notificationsResponseAfterCommentDeletion.getNotifications()).isEmpty()
        );
    }
}
