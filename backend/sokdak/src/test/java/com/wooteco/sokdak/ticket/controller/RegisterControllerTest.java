package com.wooteco.sokdak.ticket.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class RegisterControllerTest extends ControllerTest {

    @DisplayName("인증코드가 일치하면 204 반환")
    @Test
    void verifyAuthCode() {
        VerificationRequest verificationRequest = new VerificationRequest("test@gmail.com", "a1b2c3");

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(verificationRequest)
                .when().post("/members/signup/email/verification")
                .then().log().all()
                .assertThat()
                .apply(document("member/verification/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("인증코드가 일치하지 않거나 만료되었으면 400 반환")
    @Test
    void verifyAuthCode_Exception_different() {
        VerificationRequest verificationRequest = new VerificationRequest("test@gmail.com", "a1b2c3");
        doThrow(new InvalidAuthCodeException())
                .when(registerService)
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
