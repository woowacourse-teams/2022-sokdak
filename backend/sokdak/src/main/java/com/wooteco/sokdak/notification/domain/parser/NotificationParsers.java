package com.wooteco.sokdak.notification.domain.parser;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.excpetion.NotificationNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class NotificationParsers {

    private static final List<NotificationHandler> NOTIFICATION_HANDLERS = List.of(
            new NewCommentNotificationHandler(),
            new NewReplyNotificationHandler(),
            new HotBoardNotificationEventHandler(),
            new PostReportNotificationEventHandler(),
            new CommentReportNotificationEventHandler()
    );

    public boolean isNotifiable(NotificationEvent notificationEvent) {
        Optional<NotificationHandler> handler = findNotificationHandler(notificationEvent);
        return handler.isPresent() && handler.get().isNotifiable(notificationEvent);
    }

    public Notification parseToNotification(NotificationEvent notificationEvent) {
        NotificationHandler notificationHandler = findNotificationHandler(notificationEvent)
                .orElseThrow(NotificationNotFoundException::new);
        return notificationHandler.parse(notificationEvent);
    }

    private Optional<NotificationHandler> findNotificationHandler(NotificationEvent notificationEvent) {
        return NOTIFICATION_HANDLERS.stream()
                .filter(notificationHandler -> notificationHandler.isNotifiable(notificationEvent))
                .findAny();
    }
}
