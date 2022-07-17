package com.wooteco.sokdak.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.SignupHelper;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("존재하는 회원 정보로 로그인 할 수 있다.")
    @Test
    void login() {
        SignupHelper.signUp();
        LoginRequest loginRequest = new LoginRequest(SignupHelper.USERNAME, SignupHelper.PASSWORD);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.header("Set-Cookie")).contains("JSESSIONID")
        );
    }

    @DisplayName("존재하지 않는 회원 정보로 로그인할 수 없다.")
    @Test
    void login_Exception_NotExistUser() {
        LoginRequest loginRequest = new LoginRequest(SignupHelper.USERNAME, SignupHelper.PASSWORD);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
