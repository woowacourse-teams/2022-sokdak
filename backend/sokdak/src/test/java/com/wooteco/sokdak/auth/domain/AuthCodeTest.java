package com.wooteco.sokdak.auth.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.ticket.domain.AuthCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthCodeTest {

    @DisplayName("인증코드 생성시간으로부터 5분 내에 인증이 가능하다")
    @Test
    void verifyTime() {
        AuthCode authCode = AuthCode.builder()
                .code("ABCDEF")
                .serialNumber("21f46568bf6002c23843d198af30bb2bc8123695bd3d12ce86e0fc35bc5d3279")
                .createdAt(LocalDateTime.parse("2007-12-03T10:15:30"))
                .build();

        assertDoesNotThrow(() -> authCode.verifyTime(LocalDateTime.parse("2007-12-03T10:19:30")));
    }

    @DisplayName("인증코드 생성시간으로부터 5분이 지나면 인증이 불가능하다")
    @Test
    void verifyTime_Exception_Time() {
        AuthCode authCode = AuthCode.builder()
                .code("ABCDEF")
                .serialNumber("21f46568bf6002c23843d198af30bb2bc8123695bd3d12ce86e0fc35bc5d3279")
                .createdAt(LocalDateTime.parse("2007-12-03T10:15:30"))
                .build();

        assertThatThrownBy(() -> authCode.verifyTime(LocalDateTime.parse("2007-12-03T10:50:30")))
                .isInstanceOf(InvalidAuthCodeException.class);
    }
}
