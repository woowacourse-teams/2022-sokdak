package com.wooteco.sokdak.auth.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.APPLICANT_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

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

    @DisplayName("지원자 권한으로 사용할 수 없는 기능이면 예외가 발생한다.")
    @Test
    void checkAllowedApiToApplicantUser_Exception() {
        assertThatThrownBy(
                () -> authService.checkAuthority(
                        new AuthInfo(1L, RoleType.APPLICANT.getName(), "applicant"), 1L))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("사용할 수 있는 기능이면 예외가 발생하지 않는다.")
    @ParameterizedTest
    @CsvSource({"APPLICANT, 5", "USER, 5", "ADMIN, 5"})
    void checkAllowedApiToApplicantUser(String role, Long boardId) {
        assertDoesNotThrow(
                () -> authService.checkAuthority(new AuthInfo(1L, role, "applicant"), boardId));
    }
}
