package com.wooteco.sokdak.notification.handler;

import com.wooteco.sokdak.event.NotificationEvent;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import java.util.Objects;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async("asyncExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class NewNotificationEventHandler {

    private final NotificationRepository notificationRepository;

    public NewNotificationEventHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @TransactionalEventListener
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        if (isNotifiable(notificationEvent)) {
            Notification notification = new Notification(
                    notificationEvent.getNotificationType(),
                    notificationEvent.getNotificationTargetMemberId(),
                    notificationEvent.getPostId(),
                    notificationEvent.getNotificationAdditionalContentDataId()
            );
            notificationRepository.save(notification);
        }
    }

    private boolean isNotifiable(NotificationEvent notificationEvent) {
        if (Objects.isNull(notificationEvent.getEventTriggeringMemberId())) {
            return true;
        }
        return !notificationEvent.getNotificationTargetMemberId()
                .equals(notificationEvent.getEventTriggeringMemberId());
    }
}
