package com.wooteco.sokdak.board.domain;

import com.wooteco.sokdak.event.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class HotBoardEvent extends NotificationEvent {

    public HotBoardEvent(Long notificationTargetMemberId, Long postId) {
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
        return NotificationType.HOT_BOARD;
    }
}
