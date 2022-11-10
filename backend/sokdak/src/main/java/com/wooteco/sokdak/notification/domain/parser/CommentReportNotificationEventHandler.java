package com.wooteco.sokdak.notification.domain.parser;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class CommentReportNotificationEventHandler implements NotificationHandler {

    @Override
    public boolean isNotifiable(NotificationEvent notificationEvent) {
        return notificationEvent.getNotificationType() == NotificationType.COMMENT_REPORT;
    }

    @Override
    public Notification parse(NotificationEvent notificationEvent) {
        return Notification.builder()
                .member(notificationEvent.getMember())
                .post(notificationEvent.getPost())
                .comment(notificationEvent.getComment())
                .notificationType(NotificationType.COMMENT_REPORT)
                .build();
    }
}
