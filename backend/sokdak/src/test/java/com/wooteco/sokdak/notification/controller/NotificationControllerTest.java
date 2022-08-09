package com.wooteco.sokdak.notification.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.util.ControllerTest;
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
                .statusCode(HttpStatus.OK.value());
    }
}
