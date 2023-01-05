package com.wooteco.sokdak.event;

import com.wooteco.sokdak.notification.domain.NotificationType;

public abstract class NotificationEvent {

    protected final Long notificationTargetMemberId;
    private final Long postId;

    protected NotificationEvent(Long notificationTargetMemberId, Long postId) {
        this.notificationTargetMemberId = notificationTargetMemberId;
        this.postId = postId;
    }

    public Long getNotificationTargetMemberId() {
        return notificationTargetMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public abstract Long getNotificationAdditionalContentDataId();

    public abstract boolean isNotifiable();

    public abstract NotificationType getNotificationType();
}
