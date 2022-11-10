package com.wooteco.sokdak.notification.domain.parser;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationEvent;

public interface NotificationHandler {

    boolean isNotifiable(NotificationEvent notificationEvent);

    Notification parse(NotificationEvent notificationEvent);
}
