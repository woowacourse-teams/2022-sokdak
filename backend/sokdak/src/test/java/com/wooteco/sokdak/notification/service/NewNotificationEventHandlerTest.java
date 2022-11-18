package com.wooteco.sokdak.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.board.event.PostHotBoardEvent;
import com.wooteco.sokdak.comment.event.NewCommentEvent;
import com.wooteco.sokdak.comment.event.NewReplyEvent;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.report.event.CommentReportEvent;
import com.wooteco.sokdak.report.event.PostReportEvent;
import com.wooteco.sokdak.util.DatabaseCleaner;
import com.wooteco.sokdak.util.ServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class NewNotificationEventHandlerTest extends ServiceTest {

    private static final long TARGET_MEMBER_ID = 1L;
    private static final long POST_ID = 1L;
    private static final long COMMENT_ID = 1L;

    @Autowired
    private NewNotificationEventHandler newNotificationEventHandler;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanUp() {
        databaseCleaner.clear();
    }

    @DisplayName("댓글 작성자와 게시글 작성자가 다르다면 NEW_COMMENT 알림을 저장한다.")
    @Test
    void handleNewCommentNotification() {
        Long differentCommentWriterId = 2L;
        NewCommentEvent newCommentEvent =
                new NewCommentEvent(TARGET_MEMBER_ID, POST_ID, COMMENT_ID, differentCommentWriterId);

        newNotificationEventHandler.handleNewCommentNotification(newCommentEvent);

        assertThat(notificationRepository.findAll()).hasSize(1);
    }

    @DisplayName("댓글 작성자와 게시글 작성자가 같다면 NEW_COMMENT 알림을 저장하지 않는다.")
    @Test
    @Transactional
    void handleNewCommentNotification_Unsaved() {
        Long sameCommentWriterIdWithPostWriterId = TARGET_MEMBER_ID;
        NewCommentEvent newCommentEvent =
                new NewCommentEvent(TARGET_MEMBER_ID, POST_ID, COMMENT_ID, sameCommentWriterIdWithPostWriterId);

        newNotificationEventHandler.handleNewCommentNotification(newCommentEvent);

        assertThat(notificationRepository.findAll()).isEmpty();
    }

    @DisplayName("대댓글 작성자와 댓글 작성자가 다르다면 NEW_REPLY 알림을 저장한다.")
    @Test
    void handleNewReplyNotification() {
        Long differentReplyWriterId = 2L;
        NewReplyEvent newReplyEvent = new NewReplyEvent(TARGET_MEMBER_ID, POST_ID, COMMENT_ID, differentReplyWriterId);

        newNotificationEventHandler.handleNewReplyNotification(newReplyEvent);

        assertThat(notificationRepository.findAll()).hasSize(1);
    }

    @DisplayName("대댓글 작성자와 댓글 작성자가 같다면 NEW_REPLY 알림을 저장하지 않는다.")
    @Test
    void handleNewReplyNotification_Unsaved() {
        Long sameReplyWriterIdWithCommentWriterId = TARGET_MEMBER_ID;
        NewReplyEvent newReplyEvent =
                new NewReplyEvent(TARGET_MEMBER_ID, POST_ID, COMMENT_ID, sameReplyWriterIdWithCommentWriterId);

        newNotificationEventHandler.handleNewReplyNotification(newReplyEvent);

        assertThat(notificationRepository.findAll()).isEmpty();
    }

    @DisplayName("HOT_BOARD 알림을 저장한다.")
    @Test
    void handlePostHotBoardNotification() {
        PostHotBoardEvent postHotBoardEvent = new PostHotBoardEvent(TARGET_MEMBER_ID, POST_ID);

        newNotificationEventHandler.handlePostHotBoardNotification(postHotBoardEvent);

        assertThat(notificationRepository.findAll()).hasSize(1);
    }

    @DisplayName("POST_REPORT 알림을 저장한다.")
    @Test
    void handlePostReportNotification() {
        PostReportEvent postReportEvent = new PostReportEvent(TARGET_MEMBER_ID, POST_ID);

        newNotificationEventHandler.handlePostReportNotification(postReportEvent);

        assertThat(notificationRepository.findAll()).hasSize(1);
    }

    @DisplayName("COMMENT_REPORT 알림을 저장한다.")
    @Test
    void handleCommentReportNotification() {
        CommentReportEvent commentReportEvent = new CommentReportEvent(TARGET_MEMBER_ID, POST_ID, COMMENT_ID);

        newNotificationEventHandler.handleCommentReportNotification(commentReportEvent);

        assertThat(notificationRepository.findAll()).hasSize(1);
    }
}
