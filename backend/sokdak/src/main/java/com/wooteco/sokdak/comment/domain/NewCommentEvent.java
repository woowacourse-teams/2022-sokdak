package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.event.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class NewCommentEvent extends NotificationEvent {

    private final Long commentWritingMemberId;

    public NewCommentEvent(Long notificationTargetMemberId, Long postId, Long commentWritingMemberId) {
        super(notificationTargetMemberId, postId);
        this.commentWritingMemberId = commentWritingMemberId;
    }

    @Override
    public Long getNotificationAdditionalContentDataId() {
        return null;
    }

    @Override
    public boolean isNotifiable() {
        return !notificationTargetMemberId.equals(commentWritingMemberId);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.NEW_COMMENT;
    }
}
