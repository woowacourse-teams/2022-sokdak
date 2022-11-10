package com.wooteco.sokdak.notification.domain.parser;

import static com.wooteco.sokdak.util.fixture.CommentFixture.CHRIS_REPLY;
import static com.wooteco.sokdak.util.fixture.CommentFixture.JOSH_COMMENT;
import static com.wooteco.sokdak.util.fixture.CommentFixture.JOSH_REPLY;
import static com.wooteco.sokdak.util.fixture.MemberFixture.JOSH;
import static com.wooteco.sokdak.util.fixture.PostFixture.CHRIS_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NewReplyNotificationHandlerTest {

    private final NotificationHandler notificationHandler = new NewReplyNotificationHandler();

    @DisplayName("새로운 알림을 반환해야하는지 반환한다. 게시글의 작성자와 댓글 작성자가 다를 경우에만 알림 반환")
    @ParameterizedTest
    @MethodSource("provideNewReplyAndExpectedNotifiable")
    void isNotifiable(Comment newReply, boolean expected) {
        NotificationEvent notificationEvent = NotificationEvent
                .newReply(CHRIS_POST, JOSH, JOSH_COMMENT, newReply);

        boolean actual = notificationHandler.isNotifiable(notificationEvent);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideNewReplyAndExpectedNotifiable() {
        return Stream.of(
                Arguments.of(CHRIS_REPLY, true),
                Arguments.of(JOSH_REPLY, false)
        );
    }

    @DisplayName("NewComment에 대한 NotificationEvent를 Notification으로 파싱한다.")
    @Test
    void parse() {
        NotificationEvent notificationEvent = NotificationEvent
                .newReply(CHRIS_POST, JOSH, JOSH_COMMENT, CHRIS_REPLY);

        Notification notification = notificationHandler.parse(notificationEvent);

        assertAll(
                () -> assertThat(notification.getPost()).isEqualTo(CHRIS_POST),
                () -> assertThat(notification.getMember()).isEqualTo(JOSH),
                () -> assertThat(notification.getComment()).isEqualTo(JOSH_COMMENT),
                () -> assertThat(notification.getContent()).isEqualTo(JOSH_COMMENT.getMessage()),
                () -> assertThat(notification.getNotificationType()).isEqualTo(NotificationType.NEW_REPLY)
        );
    }
}
