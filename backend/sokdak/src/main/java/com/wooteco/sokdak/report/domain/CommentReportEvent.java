package com.wooteco.sokdak.report.domain;

import com.wooteco.sokdak.event.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class CommentReportEvent extends NotificationEvent {

    private final Long commentId;

    public CommentReportEvent(Long notificationTargetMemberId, Long postId, Long commentId) {
        super(notificationTargetMemberId, postId);
        this.commentId = commentId;
    }

    @Override
    public Long getNotificationAdditionalContentDataId() {
        return commentId;
    }

    @Override
    public boolean isNotifiable() {
        return true;
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.COMMENT_REPORT;
    }
}
