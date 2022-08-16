package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_REPLY;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationType;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
import com.wooteco.sokdak.notification.excpetion.NotificationNotFoundException;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyCommentIfNotMine(Member member, Post post, Comment comment) {
        if (!comment.isAuthorized(member.getId())) {
            notify(member, post, comment, NEW_COMMENT);
        }
    }

    public void notifyCommentReport(Post post, Comment comment) {
        notify(comment.getMember(), post, comment, COMMENT_REPORT);
    }

    public void notifyHotBoard(Post post) {
        notify(post.getMember(), post, null, HOT_BOARD);
    }

    public void notifyPostReport(Post post) {
        notify(post.getMember(), post, null, POST_REPORT);
    }

    public void notifyReplyIfNotMine(Member member, Post post, Comment comment, Comment reply) {
        if (!reply.isAuthorized(member.getId())) {
            notify(member, post, comment, NEW_REPLY);
        }
    }

    private void notify(Member member, Post post, Comment comment, NotificationType notificationType) {
        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .member(member)
                .post(post)
                .comment(comment)
                .build();
        notificationRepository.save(notification);
    }

    public void deleteCommentNotification(Long commentId) {
        notificationRepository.deleteAllByCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public NewNotificationCheckResponse checkNewNotification(AuthInfo authInfo) {
        return new NewNotificationCheckResponse(
                notificationRepository.existsByMemberIdAndInquiredIsFalse(authInfo.getId()));
    }

    public NotificationsResponse findNotifications(AuthInfo authInfo, Pageable pageable) {
        Slice<Notification> foundNotifications = notificationRepository
                .findNotificationsByMemberId(authInfo.getId(), pageable);
        List<Notification> notifications = foundNotifications.getContent();
        inquireNotification(notifications);

        List<NotificationResponse> notificationResponses = notifications
                .stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new NotificationsResponse(notificationResponses, foundNotifications.isLast());
    }

    private void inquireNotification(List<Notification> notifications) {
        for (Notification notification : notifications) {
            inquire(notification);
        }
    }

    private void inquire(Notification notification) {
        if (!notification.isInquired()) {
            notification.inquire();
        }
    }

    public void deleteNotification(AuthInfo authInfo, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        notification.validateOwner(authInfo.getId());
        notificationRepository.deleteById(notificationId);
    }
}
