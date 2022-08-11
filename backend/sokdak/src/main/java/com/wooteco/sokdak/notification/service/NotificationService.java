package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationType;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
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

    public void notifyNewComment(Member member, Post post, Comment comment) {
        notify(member, post, comment, NEW_COMMENT);
    }

    public void notifyCommentReport(Member member, Post post, Comment comment) {
        notify(member, post, comment, COMMENT_REPORT);
    }

    public void notifyHotBoard(Post post) {
        notify(post.getMember(), post, null, HOT_BOARD);
    }

    public void notifyPostReport(Post post) {
        notify(post.getMember(), post, null, POST_REPORT);
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

    public NewNotificationCheckResponse checkNewNotification(AuthInfo authInfo) {
        return new NewNotificationCheckResponse(
                notificationRepository.existsByMemberIdAndInquiredIsFalse(authInfo.getId()));
    }

    public NotificationsResponse findNotifications(AuthInfo authInfo, Pageable pageable) {
        Slice<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(authInfo.getId(), pageable);
        List<NotificationResponse> notificationResponses = notifications.getContent()
                .stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new NotificationsResponse(notificationResponses, notifications.isLast());
    }
}
