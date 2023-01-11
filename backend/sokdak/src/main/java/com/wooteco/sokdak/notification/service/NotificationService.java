package com.wooteco.sokdak.notification.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
import com.wooteco.sokdak.notification.excpetion.NotificationNotFoundException;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
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
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public NotificationService(NotificationRepository notificationRepository, CommentRepository commentRepository,
                               PostRepository postRepository) {
        this.notificationRepository = notificationRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public NewNotificationCheckResponse checkNewNotification(AuthInfo authInfo) {
        return new NewNotificationCheckResponse(
                notificationRepository.existsByMemberIdAndInquiredIsFalse(authInfo.getId()));
    }

    @Transactional
    public NotificationsResponse findNotifications(AuthInfo authInfo, Pageable pageable) {
        Slice<Notification> foundNotifications = notificationRepository
                .findNotificationsByMemberId(authInfo.getId(), pageable);
        List<Notification> notifications = foundNotifications.getContent();
        if (foundNotifications.hasContent()) {
            inquireNotification(notifications);
        }
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
                .map(this::toNotificationResponse)
                .collect(Collectors.toUnmodifiableList());
        return new NotificationsResponse(notificationResponses, isLastPage);
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getNotificationType().name(),
                notification.getPostId(), notification.getCreatedAt(), getNotificationContent(notification));
    }

    private String getNotificationContent(Notification notification) {
        if (notification.isCommentNotification()) {
            return commentRepository.findByCommentId(notification.getCommentId())
                    .orElseThrow(CommentNotFoundException::new)
                    .getMessage();
        }
        return postRepository.findById(notification.getPostId())
                .orElseThrow(PostNotFoundException::new)
                .getTitle();
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
}
