package com.wooteco.sokdak.auth.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.APPLICANT_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.ticket.domain.AuthCode;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

class AuthServiceTest extends ServiceTest {

    @Autowired
    private AuthService authService;

    @DisplayName("로그인 기능")
    @Test
    void login() {
        AuthInfo authInfo = authService.login(VALID_LOGIN_REQUEST);

        assertThat(authInfo.getId()).isNotNull();
    }

    @DisplayName("지원자 아이디로 로그인")
    @Test
    void login_Applicant() {
        AuthInfo authInfo = authService.login(APPLICANT_LOGIN_REQUEST);

        assertThat(authInfo.getRole()).isEqualTo(RoleType.APPLICANT.getName());
    }

    @DisplayName("존재하지 않는 회원정보로 로그인 시 예외 발생")
    @Test
    void login_Exception() {
        assertThatThrownBy(() -> authService.login(INVALID_LOGIN_REQUEST))
                .isInstanceOf(LoginFailedException.class);
    }
}
