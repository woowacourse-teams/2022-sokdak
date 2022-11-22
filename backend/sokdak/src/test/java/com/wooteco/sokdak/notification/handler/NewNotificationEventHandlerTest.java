package com.wooteco.sokdak.notification.handler;

import static org.assertj.core.api.Assertions.assertThat;

<<<<<<< HEAD
import com.wooteco.sokdak.comment.domain.NewCommentEvent;
import com.wooteco.sokdak.event.NotificationEvent;
=======
import com.wooteco.sokdak.board.event.PostHotBoardEvent;
import com.wooteco.sokdak.comment.event.NewCommentEvent;
import com.wooteco.sokdak.comment.event.NewReplyEvent;
import com.wooteco.sokdak.config.AsyncTestConfig;
>>>>>>> e7e77f05 (refactor: NewNotificationEventHandlerTest가 ServiceTest를 상속하지 않도록 수정)
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.report.domain.PostReportEvent;
import com.wooteco.sokdak.util.DatabaseCleaner;
<<<<<<< HEAD
import com.wooteco.sokdak.util.ServiceTest;
import java.util.stream.Stream;
=======
>>>>>>> e7e77f05 (refactor: NewNotificationEventHandlerTest가 ServiceTest를 상속하지 않도록 수정)
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
=======
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> e7e77f05 (refactor: NewNotificationEventHandlerTest가 ServiceTest를 상속하지 않도록 수정)

@SpringBootTest
@Import(AsyncTestConfig.class)
class NewNotificationEventHandlerTest {

    private static final long POST_ID = 1L;

    @Autowired
    private NewNotificationEventHandler newNotificationEventHandler;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanUp() {
        databaseCleaner.clear();
    }

    @DisplayName("알림 대상 사용자와 알림을 발생시킨 사용자가 같다면 알림을 저장하지 않고 "
            + "알림 대상 사용자와 알림을 발생시킨 사용자가 다르거나, 알림을 발생시킨 사용자가 중요하지 않은 알림 이벤트라면 알림을 저장한다.")
    @ParameterizedTest
    @MethodSource("provideNotifiableNotificationEvent")
    void handleNotificationEvent_Notifiable(NotificationEvent notificationEvent, int expectedSavedNotificationSize) {
        newNotificationEventHandler.handleNotificationEvent(notificationEvent);

        assertThat(notificationRepository.findAll()).hasSize(expectedSavedNotificationSize);
    }

    private static Stream<Arguments> provideNotifiableNotificationEvent() {
        Long notificationTargetMemberId = 1L;
        Long sameNotificationTriggeringMemberId = 1L;
        Long differentNotificationTriggeringMemberId = 2L;
        return Stream.of(
                Arguments.of(new NewCommentEvent(notificationTargetMemberId, POST_ID,
                        sameNotificationTriggeringMemberId), 0),
                Arguments.of(new NewCommentEvent(notificationTargetMemberId, POST_ID,
                        differentNotificationTriggeringMemberId), 1),
                Arguments.of(new PostReportEvent(notificationTargetMemberId, POST_ID), 1)
        );
    }
}
