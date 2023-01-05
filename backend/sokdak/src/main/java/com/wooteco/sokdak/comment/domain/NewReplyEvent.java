package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.event.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class NewReplyEvent extends NotificationEvent {

    private final Long commentId;
    private final Long replyWritingMemberId;

    public NewReplyEvent(Long notificationTargetMemberId, Long postId, Long commentId, Long replyWritingMemberId) {
        super(notificationTargetMemberId, postId);
        this.commentId = commentId;
        this.replyWritingMemberId = replyWritingMemberId;
    }

    @Override
    public Long getNotificationAdditionalContentDataId() {
        return commentId;
    }

    @Override
    public boolean isNotifiable() {
        return !notificationTargetMemberId.equals(replyWritingMemberId);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.NEW_REPLY;
    }
}
