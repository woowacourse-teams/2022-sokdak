package com.wooteco.sokdak.notification.handler;

import com.wooteco.sokdak.comment.domain.CommentDeletionEvent;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.PostDeletionEvent;
import javax.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class DeletionEventHandler {

    private final NotificationRepository notificationRepository;

    public DeletionEventHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @EventListener
    public void handleCommentDeletion(CommentDeletionEvent commentDeletionEvent) {
        notificationRepository.deleteAllByCommentId(commentDeletionEvent.getCommentId());
    }

    @EventListener
    public void handlePostDeletion(PostDeletionEvent postDeletionEvent) {
        notificationRepository.deleteAllByPostId(postDeletionEvent.getPostId());
    }
}
