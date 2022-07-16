package com.wooteco.sokdak.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.member.exception.NotWootecoMemberException;
import com.wooteco.sokdak.member.exception.PasswordConfirmationException;
import com.wooteco.sokdak.member.exception.TicketUsedException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTest {

    @DisplayName("우테코 크루이면서 가입을 안한 크루이면 204 반환")
    @Test
    void sendEmail() {
        EmailRequest emailRequest = new EmailRequest("test@gmail.com");
        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .when().post("/members/signup/email")
                .then().log().all()
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("우테코 크루가 아니라면 400 반환")
    @Test
    void sendEmail_Exception_Not_Wooteco() {
        EmailRequest emailRequest = new EmailRequest("test@gmail.com");
        doThrow(new NotWootecoMemberException())
                .when(emailService)
                .sendEmail(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .when().post("/members/signup/email")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("우아한테크코스 크루가 아닙니다."))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("가입한 우테코 크루라면 400 반환")
    @Test
    void sendEmail_Exception_Duplicated() {
        EmailRequest emailRequest = new EmailRequest("test@gmail.com");
        doThrow(new TicketUsedException())
                .when(emailService)
                .sendEmail(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .when().post("/members/signup/email")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("이미 가입된 크루입니다."))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("인증코드가 일치하면 204 반환")
    @Test
    void verifyAuthCode() {
        VerificationRequest verificationRequest = new VerificationRequest("test@gmail.com", "a1b2c3");

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(verificationRequest)
                .when().post("/members/signup/email/verification")
                .then().log().all()
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("인증코드가 일치하지 않으면 400 반환")
    @Test
    void verifyAuthCode_Exception_different() {
        VerificationRequest verificationRequest = new VerificationRequest("test@gmail.com", "a1b2c3");
        doThrow(new InvalidAuthCodeException())
                .when(emailService)
                .verifyAuthCode(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(verificationRequest)
                .when().post("/members/signup/email/verification")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("잘못된 인증번호입니다."))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("아이디가 중복되지 않으면 true 반환")
    @Test
    void validateUniqueUsername_true() {
        given(memberService.checkUniqueUsername("testName"))
                .willReturn(new UniqueResponse(true));

        restDocs
                .when().get("/members/signup/exists?username=testName")
                .then().log().all()
                .assertThat()
                .body("unique", equalTo(true))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("아이디가 중복되면 false 반환")
    @Test
    void validateUniqueUsername_false() {
        given(memberService.checkUniqueUsername("testName"))
                .willReturn(new UniqueResponse(false));

        restDocs
                .when().get("/members/signup/exists?username=testName")
                .then().log().all()
                .assertThat()
                .body("unique", equalTo(false))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void validateUniqueNickname_true() {
        given(memberService.checkUniqueNickname("testNickname"))
                .willReturn(new UniqueResponse(true));

        restDocs
                .when().get("/members/signup/exists?nickname=testNickname")
                .then().log().all()
                .assertThat()
                .body("unique", equalTo(true))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void validateUniqueNickname_false() {
        given(memberService.checkUniqueNickname("testNickname"))
                .willReturn(new UniqueResponse(false));

        restDocs
                .when().get("/members/signup/exists?nickname=testNickname")
                .then().log().all()
                .assertThat()
                .body("unique", equalTo(false))
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void signUp() {
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "username", "nickname", "a1b1c1",
                "password1!", "password1!");

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void signUp_Exception_Password_Different() {
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "username", "nickname", "a1b1c1",
                "password1!", "password2!");

        doThrow(new PasswordConfirmationException())
                .when(memberService)
                .signUp(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
