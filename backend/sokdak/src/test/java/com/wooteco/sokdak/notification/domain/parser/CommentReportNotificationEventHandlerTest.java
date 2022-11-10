package com.wooteco.sokdak.notification.domain.parser;

import static com.wooteco.sokdak.util.fixture.CommentFixture.JOSH_COMMENT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.JOSH;
import static com.wooteco.sokdak.util.fixture.PostFixture.CHRIS_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentReportNotificationEventHandlerTest {

    @DisplayName("CommentReport 알림에 대한 NotificationEvent를 Notification으로 파싱한다.")
    @Test
    void parse() {
        NotificationEvent notificationEvent = NotificationEvent.commentReport(CHRIS_POST, JOSH,
                JOSH_COMMENT);
        NotificationHandler notificationHandler = new CommentReportNotificationEventHandler();

        Notification notification = notificationHandler.parse(notificationEvent);

        assertAll(
                () -> assertThat(notification.getPost()).isEqualTo(CHRIS_POST),
                () -> assertThat(notification.getMember()).isEqualTo(JOSH),
                () -> assertThat(notification.getComment()).isEqualTo(JOSH_COMMENT),
                () -> assertThat(notification.getContent()).isEqualTo(JOSH_COMMENT.getMessage()),
                () -> assertThat(notification.getNotificationType()).isEqualTo(NotificationType.COMMENT_REPORT)
        );
    }
}
