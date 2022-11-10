package com.wooteco.sokdak.notification.domain.parser;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class HotBoardNotificationEventHandler implements NotificationHandler {

    @Override
    public boolean isNotifiable(NotificationEvent notificationEvent) {
        return notificationEvent.getNotificationType() == NotificationType.HOT_BOARD;
    }

    @Override
    public Notification parse(NotificationEvent notificationEvent) {
        return Notification.builder()
                .member(notificationEvent.getMember())
                .post(notificationEvent.getPost())
                .notificationType(NotificationType.HOT_BOARD)
                .build();
    }
}
