package com.wooteco.sokdak.auth.controller;

import static com.wooteco.sokdak.util.HttpMethodFixture.httpPost;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.domain.member.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthControllerTest extends AcceptanceTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUpp() {
        Member hunch = Member.builder()
                .username("hunch123")
                .password("hunch1234@")
                .nickname("hunch")
                .build();
        memberRepository.save(hunch);
    }

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest("hunch123", "hunch1234@");
        ExtractableResponse<Response> response = httpPost(loginRequest, "/login");

    }

    @Test
    void test() {
        LoginRequest loginRequest = new LoginRequest("hunch123", "hunch1234@");
        ExtractableResponse<Response> response = httpPost(loginRequest, "/login");
        String s = response.sessionId();
        RestAssured
                .given().log().all()
                .sessionId(s)
                .when().get("/test")
                .then().log().all()
                .extract();
    }

    @Test
    void test2() {
        LoginRequest loginRequest = new LoginRequest("hunch123", "hunch1234@");
        ExtractableResponse<Response> response = httpPost(loginRequest, "/login");
        String s = response.sessionId();
        RestAssured
                .given().log().all()
                .sessionId(s)
                .when().get("/test2")
                .then().log().all()
                .extract();
    }
}
