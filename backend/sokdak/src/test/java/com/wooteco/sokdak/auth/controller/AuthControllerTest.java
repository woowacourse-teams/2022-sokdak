package com.wooteco.sokdak.auth.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.util.ControllerTest;
import com.wooteco.sokdak.util.fixture.MemberFixture;
import com.wooteco.sokdak.util.fixture.PostFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AuthControllerTest extends ControllerTest {

    @DisplayName("올바른 회원정보로 로그인하면 200 반환")
    @Test
    void login() {
        given(authService.login(any(LoginRequest.class)))
                .willReturn(MemberFixture.AUTH_INFO);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(VALID_LOGIN_REQUEST)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 회원정보로 로그인하면 400 반환")
    @Test
    void login_Exception() {
        given(authService.login(any(LoginRequest.class)))
                .willThrow(LoginFailedException.class);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(INVALID_LOGIN_REQUEST)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
