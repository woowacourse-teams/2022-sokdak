package com.wooteco.sokdak.notification.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.notification.domain.NotificationType;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
import com.wooteco.sokdak.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class NotificationControllerTest extends ControllerTest {

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("새로운 알림이 있는지 응답한다.")
    @Test
    void checkNewNotification() {
        doReturn(new NewNotificationCheckResponse(true))
                .when(notificationService)
                .checkNewNotification(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().get("/notifications/check")
                .then().log().all()
                .apply(document("notification/checkNew/success"))
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("알림을 반환한다.")
    @Test
    void findNotifications() {
        NotificationResponse notificationResponse1 = NotificationResponse.builder()
                .id(1L)
                .content("게시글 제목")
                .type(NotificationType.POST_REPORT)
                .postId(1L)
                .createdAt(LocalDateTime.now())
                .build();
        NotificationResponse notificationResponse2 = NotificationResponse.builder()
                .id(1L)
                .content("게시글 제목")
                .type(NotificationType.NEW_COMMENT)
                .postId(1L)
                .createdAt(LocalDateTime.now())
                .build();
        NotificationsResponse notificationsResponse =
                new NotificationsResponse(List.of(notificationResponse1, notificationResponse2), true);
        doReturn(notificationsResponse)
                .when(notificationService)
                .findNotifications(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().get("/notifications?size=2&page=1")
                .then().log().all()
                .apply(document("notification/findNotifications/success"))
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
