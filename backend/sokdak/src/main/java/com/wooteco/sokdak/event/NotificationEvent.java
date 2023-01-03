package com.wooteco.sokdak.event;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_REPLY;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;

import com.wooteco.sokdak.notification.domain.NotificationType;

public class NotificationEvent {

    private final NotificationType notificationType;
    private final Long notificationTargetMemberId;
    private final Long postId;
    private final Long notificationAdditionalContentDataId;
    private final Long eventTriggeringMemberId;

    private NotificationEvent(NotificationType notificationType, Long notificationTargetMemberId,
                              Long postId, Long commentId, Long eventTriggeringMemberId) {
        this.notificationType = notificationType;
        this.notificationTargetMemberId = notificationTargetMemberId;
        this.postId = postId;
        this.notificationAdditionalContentDataId = commentId;
        this.eventTriggeringMemberId = eventTriggeringMemberId;
    }

    public static NotificationEvent toNewCommentEvent(Long notificationTargetMemberId, Long postId,
                                                      Long eventTriggeringMemberId) {
        return new NotificationEvent(NEW_COMMENT, notificationTargetMemberId, postId, null, eventTriggeringMemberId);
    }

    public static NotificationEvent toNewReplyEvent(Long notificationTargetMemberId, Long postId,
                                                    Long notificationContentDataId, Long eventTriggeringMemberId) {
        return new NotificationEvent(NEW_REPLY, notificationTargetMemberId, postId,
                notificationContentDataId, eventTriggeringMemberId);
    }

    public static NotificationEvent toHotBoardEvent(Long notificationTargetMemberId, Long postId) {
        return new NotificationEvent(HOT_BOARD, notificationTargetMemberId, postId, null, null);
    }

    public static NotificationEvent toPostReportEvent(Long notificationTargetMemberId, Long postId) {
        return new NotificationEvent(POST_REPORT, notificationTargetMemberId, postId, null, null);
    }

    public static NotificationEvent toCommentReportEvent(Long notificationTargetMemberId, Long postId,
                                                         Long notificationContentDataId) {
        return new NotificationEvent(COMMENT_REPORT, notificationTargetMemberId, postId, notificationContentDataId,
                null);
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Long getNotificationTargetMemberId() {
        return notificationTargetMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getNotificationAdditionalContentDataId() {
        return notificationAdditionalContentDataId;
    }

    public Long getEventTriggeringMemberId() {
        return eventTriggeringMemberId;
    }
}
