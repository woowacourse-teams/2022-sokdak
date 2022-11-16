package com.wooteco.sokdak.notification.service;

import com.wooteco.sokdak.board.event.PostHotBoardEvent;
import com.wooteco.sokdak.comment.event.NewCommentEvent;
import com.wooteco.sokdak.comment.event.NewReplyEvent;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.report.event.CommentReportEvent;
import com.wooteco.sokdak.report.event.PostReportEvent;
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
    public void handleNewCommentNotification(NewCommentEvent newCommentEvent) {
        if (!newCommentEvent.getTargetMemberId().equals(newCommentEvent.getCommentMemberId())) {
            Notification notification =
                    Notification.newComment(newCommentEvent.getTargetMemberId(), newCommentEvent.getPostId());
            notificationRepository.save(notification);
        }
    }

    @TransactionalEventListener
    public void handleNewReplyNotification(NewReplyEvent newReplyEvent) {
        if (!newReplyEvent.getCommentMemberId().equals(newReplyEvent.getReplyMemberId())) {
            Notification notification = Notification.newReply(
                    newReplyEvent.getCommentMemberId(), newReplyEvent.getPostId(), newReplyEvent.getCommentId());
            notificationRepository.save(notification);
        }
    }

    @TransactionalEventListener
    public void handlePostHotBoardNotification(PostHotBoardEvent postHotBoardEvent) {
        Notification notification =
                Notification.postHotBoard(postHotBoardEvent.getTargetMemberId(), postHotBoardEvent.getPostId());
        notificationRepository.save(notification);
    }

    @TransactionalEventListener
    public void handlePostReportNotification(PostReportEvent postReportEvent) {
        Notification notification =
                Notification.postHotBoard(postReportEvent.getTargetMemberId(), postReportEvent.getPostId());
        notificationRepository.save(notification);
    }

    @TransactionalEventListener
    public void handleCommentReportNotification(CommentReportEvent commentReportEvent) {
        Notification notification = Notification.commentReport(commentReportEvent.getTargetMemberId(),
                commentReportEvent.getPostId(), commentReportEvent.getCommentId());
        notificationRepository.save(notification);
    }
}
