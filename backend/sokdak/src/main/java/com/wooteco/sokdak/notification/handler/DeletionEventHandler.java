package com.wooteco.sokdak.notification.handler;

import com.wooteco.sokdak.comment.event.CommentDeletionEvent;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.event.PostDeletionEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async("asyncExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class DeletionEventHandler {

    private final NotificationRepository notificationRepository;

    public DeletionEventHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @TransactionalEventListener
    public void handleCommentDeletion(CommentDeletionEvent commentDeletionEvent) {
        notificationRepository.deleteAllByCommentId(commentDeletionEvent.getCommentId());
    }

    @TransactionalEventListener
    public void handlePostDeletion(PostDeletionEvent postDeletionEvent) {
        notificationRepository.deleteAllByPostId(postDeletionEvent.getPostId());
    }
}
