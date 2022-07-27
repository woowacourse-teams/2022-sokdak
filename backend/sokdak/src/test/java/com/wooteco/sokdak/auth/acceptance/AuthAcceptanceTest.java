package com.wooteco.sokdak.auth.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.MemberFixture.INVALID_LOGIN_REQUEST;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_LOGIN_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("존재하는 회원 정보로 로그인 할 수 있다.")
    @Test
    void login() {
        ExtractableResponse<Response> response = httpPost(VALID_LOGIN_REQUEST, "/login");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.header(AUTHORIZATION)).contains("Bearer")
        );
    }

    @DisplayName("존재하지 않는 회원 정보로 로그인할 수 없다.")
    @Test
    void login_Exception_NotExistUser() {
        ExtractableResponse<Response> response = httpPost(INVALID_LOGIN_REQUEST, "/login");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
