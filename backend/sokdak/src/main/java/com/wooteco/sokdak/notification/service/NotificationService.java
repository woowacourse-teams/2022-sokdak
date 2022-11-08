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
import com.wooteco.sokdak.notification.repository.NewNotificationExistenceRepository;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NewNotificationExistenceRepository newNotificationExistenceRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               NewNotificationExistenceRepository newNotificationExistenceRepository) {
        this.notificationRepository = notificationRepository;
        this.newNotificationExistenceRepository = newNotificationExistenceRepository;
    }

    @Transactional
    public void notifyCommentIfNotMine(Member member, Post post, Comment comment) {
        if (!comment.isAuthorized(member.getId())) {
            notify(member, post, comment, NEW_COMMENT);
        }
    }

    @Transactional
    public void notifyCommentReport(Post post, Comment comment) {
        notify(comment.getMember(), post, comment, COMMENT_REPORT);
    }

    @Transactional
    public void notifyHotBoard(Post post) {
        notify(post.getMember(), post, null, HOT_BOARD);
    }

    @Transactional
    public void notifyPostReport(Post post) {
        notify(post.getMember(), post, null, POST_REPORT);
    }

    @Transactional
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
        newNotificationExistenceRepository.update(member.getId(), true);
    }

    public NewNotificationCheckResponse checkNewNotification(AuthInfo authInfo) {
        return new NewNotificationCheckResponse(
                newNotificationExistenceRepository.existsByMemberIdAndExistenceIsTrue(authInfo.getId()));
    }

    @Transactional
    public NotificationsResponse findNotifications(AuthInfo authInfo, Pageable pageable) {
        Slice<Notification> foundNotifications = notificationRepository
                .findNotificationsByMemberId(authInfo.getId(), pageable);
        List<Notification> notifications = foundNotifications.getContent();
        if (foundNotifications.hasContent()) {
            inquireNotification(notifications);
        }
        boolean newNotificationExistence = notificationRepository.existsByMemberIdAndInquiredIsFalse(authInfo.getId());
        newNotificationExistenceRepository.update(authInfo.getId(), newNotificationExistence);
        return findNotifications(notifications, foundNotifications.isLast());
    }

    private void inquireNotification(List<Notification> notifications) {
        List<Long> inquiredNotificationIds = notifications.stream()
                .map(Notification::getId)
                .collect(Collectors.toUnmodifiableList());
        notificationRepository.inquireNotificationByIds(inquiredNotificationIds);
    }

    private NotificationsResponse findNotifications(List<Notification> notifications, boolean isLastPage) {
        List<NotificationResponse> notificationResponses = notifications
                .stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new NotificationsResponse(notificationResponses, isLastPage);
    }

    @Transactional
    public void deleteNotification(AuthInfo authInfo, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        notification.validateOwner(authInfo.getId());
        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    public void deleteCommentNotification(Long commentId) {
        List<Long> ids = notificationRepository.findIdsByCommentId(commentId);
        if (!ids.isEmpty()) {
            notificationRepository.deleteAllById(ids);
        }
    }

    @Transactional
    public void deletePostNotification(Long postId) {
        List<Long> ids = notificationRepository.findIdsByPostId(postId);
        if (!ids.isEmpty()) {
            notificationRepository.deleteAllById(ids);
        }
    }
}
