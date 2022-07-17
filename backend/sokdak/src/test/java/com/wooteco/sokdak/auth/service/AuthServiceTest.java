package com.wooteco.sokdak.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.wooteco.sokdak.auth.SignupHelper;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @DisplayName("로그인 기능")
    @Test
    void login() {
        SignupHelper.signUp();
        LoginRequest loginRequest = new LoginRequest(SignupHelper.USERNAME, SignupHelper.PASSWORD);

        AuthInfo authInfo = authService.login(loginRequest);

        assertThat(authInfo.getId()).isNotNull();
    }

    @DisplayName("존재하지 않는 회원정보로 로그인 시 예외 발생")
    @Test
    void login_Exception() {
        LoginRequest loginRequest = new LoginRequest(SignupHelper.USERNAME, SignupHelper.PASSWORD);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(LoginFailedException.class);
    }
}
