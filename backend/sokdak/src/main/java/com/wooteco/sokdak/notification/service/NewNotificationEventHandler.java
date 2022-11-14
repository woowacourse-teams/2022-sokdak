package com.wooteco.sokdak.notification.service;

import com.wooteco.sokdak.comment.event.NewCommentEvent;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async("asyncExecutor")
public class NewNotificationEventHandler {

    private final NotificationRepository notificationRepository;

    public NewNotificationEventHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @TransactionalEventListener
    public void handleNewCommentNotification(NewCommentEvent newCommentEvent) {
        if (!newCommentEvent.getTargetMemberId().equals(newCommentEvent.getCommentMemberId())) {
            Notification notification =
                    Notification.newComment(newCommentEvent.getTargetMemberId(), newCommentEvent.getPostId());
            notificationRepository.save(notification);
        }
    }
}
