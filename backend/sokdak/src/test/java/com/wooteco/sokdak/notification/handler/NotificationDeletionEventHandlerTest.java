package com.wooteco.sokdak.notification.handler;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_REPLY;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.comment.domain.CommentDeletionEvent;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.PostDeletionEvent;
import com.wooteco.sokdak.util.DatabaseCleaner;
import com.wooteco.sokdak.util.ServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NotificationDeletionEventHandlerTest extends ServiceTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationDeletionEventHandler notificationDeletionEventHandler;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanUp() {
        databaseCleaner.clear();
    }

    @DisplayName("게시글에 해당하는 알림을 모두 삭제한다.")
    @Test
    void handlePostDeletion() {
        Long postId = 1L;
        notificationRepository.save(new Notification(HOT_BOARD, 1L, postId, null));
        notificationRepository.save(new Notification(NEW_COMMENT, 1L, postId, null));
        PostDeletionEvent postDeletionEvent = new PostDeletionEvent(postId);

        notificationDeletionEventHandler.handlePostDeletion(postDeletionEvent);

        assertThat(notificationRepository.findAll()).isEmpty();
    }

    @DisplayName("댓글에 해당하는 알림을 모두 삭제한다.")
    @Test
    void handleCommentDeletion() {
        Long commentId = 1L;
        notificationRepository.save(new Notification(COMMENT_REPORT, 1L, 1L, commentId));
        notificationRepository.save(new Notification(NEW_REPLY, 1L, 1L, commentId));
        CommentDeletionEvent commentDeletionEvent = new CommentDeletionEvent(commentId);

        notificationDeletionEventHandler.handleCommentDeletion(commentDeletionEvent);

        assertThat(notificationRepository.findAll()).isEmpty();
    }
}

