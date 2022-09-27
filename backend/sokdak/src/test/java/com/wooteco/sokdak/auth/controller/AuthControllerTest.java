package com.wooteco.sokdak.auth.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.CHRIS_ID;
import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AuthControllerTest extends ControllerTest {

    @DisplayName("올바른 회원정보로 로그인하면 200 반환")
    @Test
    void login() {
        AuthInfo authInfo = new AuthInfo(CHRIS_ID, RoleType.USER.getName(), "chrisNickname");
        given(authService.login(any(LoginRequest.class)))
                .willReturn(authInfo);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(VALID_LOGIN_REQUEST)
                .when().post("/login")
                .then().log().all()
                .assertThat()
                .apply(document("login/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 회원정보로 로그인하면 401 반환")
    @Test
    void login_Exception() {
        doThrow(new LoginFailedException())
                .when(authService).login(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(INVALID_LOGIN_REQUEST)
                .when().post("/login")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("아이디나 비밀번호가 잘못되었습니다"))
                .apply(document("login/fail"))
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @DisplayName("로그아웃하면 204 반환")
    @Test
    void logout() {
        doReturn(true)
                .when(authInterceptor)
                        .preHandle(any(),any(),any());

        restDocs
                .header("Authorization", "any")
                .when().get("/logout")
                .then().log().all()
                .assertThat()
                .apply(document("logout/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("리프레시 요청을 받으면 204 반환")
    @Test
    void refresh() {
        restDocs
                .header("Authorization", "any")
                .header("Refresh-Token", "any")
                .when().get("/refresh")
                .then().log().all()
                .assertThat()
                .apply(document("refresh/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("인증코드가 일치하지 않거나 만료되었으면 400 반환")
    @Test
    void verifyAuthCode_Exception_different() {
        VerificationRequest verificationRequest = new VerificationRequest("test@gmail.com", "a1b2c3");
        doThrow(new InvalidAuthCodeException())
                .when(authService)
                .verifyAuthCode(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(verificationRequest)
                .when().post("/members/signup/email/verification")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("잘못된 인증번호입니다."))
                .apply(document("member/verification/fail"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
