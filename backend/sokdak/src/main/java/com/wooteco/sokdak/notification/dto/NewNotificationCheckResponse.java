package com.wooteco.sokdak.notification.dto;

import lombok.Getter;

@Getter
public class NewNotificationCheckResponse {

    private boolean existence;

    public NewNotificationCheckResponse() {
    }

    public NewNotificationCheckResponse(boolean existence) {
        this.existence = existence;
    }
}
