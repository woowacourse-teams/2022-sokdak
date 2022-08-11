package com.wooteco.sokdak.auth.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.domain.AuthCode;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

@DisplayName("인증 관련 인수테스트")
class AuthAcceptanceTest extends AcceptanceTest {

    private static final Clock FUTURE_CLOCK = Clock.fixed(Instant.parse("3333-08-22T10:00:00Z"), ZoneOffset.UTC);

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @SpyBean
    private Clock clock;

    @DisplayName("존재하는 회원 정보로 로그인 할 수 있다s.")
    @Test
    void login() {
        ExtractableResponse<Response> response = httpPost(VALID_LOGIN_REQUEST, "/login");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.header(AUTHORIZATION)).contains("Bearer"),
                () -> assertThat(response.header("Refresh-Token")).contains("Bearer")
        );
    }

    @DisplayName("존재하지 않는 회원 정보로 로그인할 수 없다.")
    @Test
    void login_Exception_NotExistUser() {
        ExtractableResponse<Response> response = httpPost(INVALID_LOGIN_REQUEST, "/login");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("올바른 인증번호로 인증할 수 있다.")
    @Test
    void verifyAuthCode() {
        String email = "sokdak@gmail.com";
        String code = "ABCDEF";
        AuthCode authCode = AuthCode.builder()
                .code(code)
                .serialNumber(Encryptor.encrypt(email))
                .build();
        authCodeRepository.save(authCode);

        ExtractableResponse<Response> response = httpPostWithAuthorization(new VerificationRequest(email, code),
                "members/signup/email/verification", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("잘못된 인증번호로 인증할 수 없다.")
    @Test
    void verifyAuthCode_Exception_WrongCode() {
        String email = "sokdak@gmail.com";
        String code = "ABCDEF";
        AuthCode authCode = AuthCode.builder()
                .code(code)
                .serialNumber(Encryptor.encrypt(email))
                .build();
        authCodeRepository.save(authCode);

        ExtractableResponse<Response> response = httpPostWithAuthorization(new VerificationRequest(email, "NONONO"),
                "members/signup/email/verification", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 이메일로 인증할 수 없다.")
    @Test
    void verifyAuthCode_Exception_WrongEmail() {
        String email = "sokdak@gmail.com";
        String code = "ABCDEF";
        AuthCode authCode = AuthCode.builder()
                .code(code)
                .serialNumber(Encryptor.encrypt(email))
                .build();
        authCodeRepository.save(authCode);

        ExtractableResponse<Response> response = httpPostWithAuthorization(new VerificationRequest("wrong@gmail.com", code),
                "members/signup/email/verification", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("만료된 인증번호로 인증할 수 없다.")
    @Test
    void verifyAuthCode_Exception_Expired() {
        String email = "sokdak@gmail.com";
        String code = "ABCDEF";
        AuthCode authCode = AuthCode.builder()
                .code(code)
                .serialNumber(Encryptor.encrypt(email))
                .build();
        authCodeRepository.save(authCode);

        doReturn(Instant.now(FUTURE_CLOCK))
                .when(clock)
                .instant();
        ExtractableResponse<Response> response = httpPostWithAuthorization(new VerificationRequest(email, code),
                "members/signup/email/verification", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
