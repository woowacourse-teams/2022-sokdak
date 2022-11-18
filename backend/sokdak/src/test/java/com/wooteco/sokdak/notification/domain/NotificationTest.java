package com.wooteco.sokdak.notification.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @DisplayName("알림의 주인이 아니면 예외를 반환한다.")
    @Test
    void validateOwner() {
        Long memberId = 1L;
        Notification notification = Notification.newComment(memberId, 1L);

        assertThatThrownBy(() -> notification.validateOwner(9999L))
                .isInstanceOf(AuthorizationException.class);
    }
}
