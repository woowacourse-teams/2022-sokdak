package com.wooteco.sokdak.notification.excpetion;

import com.wooteco.sokdak.advice.NotFoundException;

public class NotificationNotFoundException extends NotFoundException {

    private static final String MESSAGE = "알림을 찾을 수 없습니다.";

    public NotificationNotFoundException() {
        super(MESSAGE);
    }
}
