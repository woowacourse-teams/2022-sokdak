package com.wooteco.sokdak.notification.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class NotificationsResponse {

    private List<NotificationResponse> notifications;
    private boolean lastPage;

    public NotificationsResponse() {
    }

    public NotificationsResponse(List<NotificationResponse> notifications, boolean lastPage) {
        this.notifications = notifications;
        this.lastPage = lastPage;
    }
}
