package com.wooteco.sokdak.report.domain;

import com.wooteco.sokdak.event.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class PostReportEvent extends NotificationEvent {

    public PostReportEvent(Long notificationTargetMemberId, Long postId) {
        super(notificationTargetMemberId, postId);
    }

    @Override
    public Long getNotificationAdditionalContentDataId() {
        return null;
    }

    @Override
    public boolean isNotifiable() {
        return true;
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.POST_REPORT;
    }
}
