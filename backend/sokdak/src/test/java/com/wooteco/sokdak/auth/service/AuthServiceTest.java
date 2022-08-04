package com.wooteco.sokdak.auth.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.auth.domain.AuthCode;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

class AuthServiceTest extends IntegrationTest {

    private static final String EMAIL = "test@gmail.com";
    private static final String AUTH_CODE = "ABCDEF";
    private static final Clock FUTURE_CLOCK = Clock.fixed(Instant.parse("3333-08-22T10:00:00Z"), ZoneOffset.UTC);
    @Autowired
    private AuthService authService;
    @Autowired
    private Encryptor encryptor;
    @Autowired
    private AuthCodeRepository authCodeRepository;

    @SpyBean
    private Clock clock;

    @DisplayName("로그인 기능")
    @Test
    void login() {
        AuthInfo authInfo = authService.login(VALID_LOGIN_REQUEST);

        assertThat(authInfo.getId()).isNotNull();
    }

    @DisplayName("존재하지 않는 회원정보로 로그인 시 예외 발생")
    @Test
    void login_Exception() {
        assertThatThrownBy(() -> authService.login(INVALID_LOGIN_REQUEST))
                .isInstanceOf(LoginFailedException.class);
    }

    @DisplayName("인증번호 만료 여부 검증 기능")
    @Test
    void verifyAuthCode() {
        AuthCode authCode = AuthCode.builder()
                .code(AUTH_CODE)
                .serialNumber(encryptor.encrypt(EMAIL))
                .build();
        authCodeRepository.save(authCode);

        VerificationRequest verificationRequest = new VerificationRequest(EMAIL, AUTH_CODE);
        assertDoesNotThrow(() -> authService.verifyAuthCode(verificationRequest));
    }

    @DisplayName("잘못된 인증번호일 시 예외 발생")
    @Test
    void verifyAuthCode_Exception_WrongAuthCode() {
        AuthCode authCode = AuthCode.builder()
                .code(AUTH_CODE)
                .serialNumber(encryptor.encrypt(EMAIL))
                .build();
        authCodeRepository.save(authCode);

        VerificationRequest verificationRequest = new VerificationRequest(EMAIL, "NONONO");
        assertThatThrownBy(() -> authService.verifyAuthCode(verificationRequest))
                .isInstanceOf(InvalidAuthCodeException.class);
    }

    @DisplayName("인증번호와 이메일이 매칭되지 않을 시 예외 발생")
    @Test
    void verifyAuthCode_Exception_WrongEmail() {
        AuthCode authCode = AuthCode.builder()
                .code(AUTH_CODE)
                .serialNumber(encryptor.encrypt(EMAIL))
                .build();
        authCodeRepository.save(authCode);

        VerificationRequest verificationRequest = new VerificationRequest("wrong@gmail.com", AUTH_CODE);
        assertThatThrownBy(() -> authService.verifyAuthCode(verificationRequest))
                .isInstanceOf(SerialNumberNotFoundException.class);
    }

    @DisplayName("인증번호 만료 시 예외 발생")
    @Test
    void verifyAuthCode_Exception_Expired() {
        AuthCode authCode = AuthCode.builder()
                .code(AUTH_CODE)
                .serialNumber(encryptor.encrypt(EMAIL))
                .build();
        authCodeRepository.save(authCode);

        doReturn(Instant.now(FUTURE_CLOCK))
                .when(clock)
                .instant();

        VerificationRequest verificationRequest = new VerificationRequest(EMAIL, AUTH_CODE);
        assertThatThrownBy(() -> authService.verifyAuthCode(verificationRequest))
                .isInstanceOf(InvalidAuthCodeException.class);
    }
}
