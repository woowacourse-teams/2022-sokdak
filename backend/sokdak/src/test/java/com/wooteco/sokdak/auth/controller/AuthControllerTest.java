package com.wooteco.sokdak.auth.controller;

import static org.mockito.ArgumentMatchers.any;

import com.wooteco.sokdak.auth.SignupHelper;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AuthControllerTest extends ControllerTest {

    @DisplayName("올바른 회원정보로 로그인하면 200 반환")
    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest(SignupHelper.USERNAME, SignupHelper.PASSWORD);
        AuthInfo authInfo = new AuthInfo(1L);
        BDDMockito.given(authService.login(any(LoginRequest.class)))
                        .willReturn(authInfo);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 회원정보로 로그인하면 400 반환")
    @Test
    void login_Exception() {
        LoginRequest loginRequest = new LoginRequest("invalidUsername", "invalidPassword");
        BDDMockito.given(authService.login(any(LoginRequest.class)))
                .willThrow(LoginFailedException.class);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
