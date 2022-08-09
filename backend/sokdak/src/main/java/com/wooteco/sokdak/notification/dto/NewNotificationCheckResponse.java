package com.wooteco.sokdak.notification.dto;

import lombok.Getter;

@Getter
public class NewNotificationCheckResponse {

    private final boolean existence;

    public NewNotificationCheckResponse(boolean existence) {
        this.existence = existence;
    }
}
