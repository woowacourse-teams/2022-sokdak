package com.wooteco.sokdak.notification.domain.parser;

import static com.wooteco.sokdak.util.fixture.MemberFixture.CHRIS;
import static com.wooteco.sokdak.util.fixture.PostFixture.CHRIS_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostReportNotificationEventHandlerTest {

    @DisplayName("CommentReport 알림에 대한 NotificationEvent를 Notification으로 파싱한다.")
    @Test
    void parse() {
        NotificationEvent notificationEvent = NotificationEvent.postReport(CHRIS_POST, CHRIS);
        NotificationHandler notificationHandler = new PostReportNotificationEventHandler();

        Notification notification = notificationHandler.parse(notificationEvent);

        assertAll(
                () -> assertThat(notification.getPost()).isEqualTo(CHRIS_POST),
                () -> assertThat(notification.getMember()).isEqualTo(CHRIS),
                () -> assertThat(notification.getComment()).isNull(),
                () -> assertThat(notification.getContent()).isEqualTo(CHRIS_POST.getTitle()),
                () -> assertThat(notification.getNotificationType()).isEqualTo(NotificationType.POST_REPORT)
        );
    }
}
