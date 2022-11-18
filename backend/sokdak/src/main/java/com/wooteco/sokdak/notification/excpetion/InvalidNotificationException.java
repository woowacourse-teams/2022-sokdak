package com.wooteco.sokdak.notification.excpetion;

import com.wooteco.sokdak.advice.InternalException;

public class InvalidNotificationException extends InternalException {

    private static final String MESSAGE = "허용되지 않은 알림 종류입니다.";

    public InvalidNotificationException() {
        super(MESSAGE);
    }
}
