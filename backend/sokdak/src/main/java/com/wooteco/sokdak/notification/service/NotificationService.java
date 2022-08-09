package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_REPLY;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.REPLY_REPORT;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationType;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyNewComment(Member member, Post post, String content) {
        notify(member, post, content, NEW_COMMENT);
    }

    public void notifyCommentReport(Member member, Post post, String content) {
        notify(member, post, content, COMMENT_REPORT);
    }

    public void notifyNewReply(Member member, Post post, String content) {
        notify(member, post, content, NEW_REPLY);
    }

    public void notifyReplyReport(Member member, Post post, String content) {
        notify(member, post, content, REPLY_REPORT);
    }

    public void notifyHotBoard(Member member, Post post) {
        notify(member, post, null, HOT_BOARD);
    }

    public void notifyPostReport(Member member, Post post) {
        notify(member, post, null, POST_REPORT);
    }

    private void notify(Member member, Post post, String content, NotificationType notificationType) {
        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .member(member)
                .post(post)
                .content(content)
                .build();
        notificationRepository.save(notification);
    }
}
