package com.wooteco.sokdak.notification.domain.parser;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class PostReportNotificationEventHandler implements NotificationHandler {

    @Override
    public boolean isNotifiable(NotificationEvent notificationEvent) {
        return notificationEvent.getNotificationType() == NotificationType.POST_REPORT;
    }

    @Override
    public Notification parse(NotificationEvent notificationEvent) {
        return Notification.builder()
                .member(notificationEvent.getMember())
                .post(notificationEvent.getPost())
                .notificationType(NotificationType.POST_REPORT)
                .build();
    }
}
