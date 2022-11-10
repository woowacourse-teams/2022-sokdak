package com.wooteco.sokdak.notification.service;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.parser.NotificationParsers;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NewNotificationEventHandler {

    private final NotificationService notificationService;
    private final NotificationParsers notificationParsers;

    public NewNotificationEventHandler(NotificationService notificationService,
                                       NotificationParsers notificationParsers) {
        this.notificationService = notificationService;
        this.notificationParsers = notificationParsers;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void saveNewNotification(NotificationEvent notificationEvent) {
        if (notificationParsers.isNotifiable(notificationEvent)) {
            Notification notification = notificationParsers.parseToNotification(notificationEvent);
            notificationService.notify(notification);
        }
    }
}

