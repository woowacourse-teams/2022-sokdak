package com.wooteco.sokdak.notification.domain.parser;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;
import com.wooteco.sokdak.notification.domain.NotificationType;

public class NewReplyNotificationHandler implements NotificationHandler {

    @Override
    public boolean isNotifiable(NotificationEvent notificationEvent) {
        if (notificationEvent.getNotificationType() != NotificationType.NEW_REPLY) {
            return false;
        }
        Comment reply = notificationEvent.getReply();
        Member notifyingTarget = notificationEvent.getMember();
        return !reply.isAuthorized(notifyingTarget.getId());
    }

    @Override
    public Notification parse(NotificationEvent notificationEvent) {
        return Notification.builder()
                .member(notificationEvent.getMember())
                .post(notificationEvent.getPost())
                .comment(notificationEvent.getComment())
                .notificationType(NotificationType.NEW_REPLY)
                .build();
    }
}
