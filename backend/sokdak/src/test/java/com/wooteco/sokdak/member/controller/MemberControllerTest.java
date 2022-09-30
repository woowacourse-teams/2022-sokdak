package com.wooteco.sokdak.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.NicknameResponse;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.DuplicateNicknameException;
import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.member.exception.InvalidSignupFlowException;
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
                .assertThat()
                .apply(document("member/email/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("우테코 크루가 아니라면 400 반환")
    @Test
    void sendEmail_Exception_Not_Wooteco() {
        EmailRequest emailRequest = new EmailRequest("test@gmail.com");
        doThrow(new NotWootecoMemberException())
                .when(emailService)
                .sendCodeToValidUser(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .when().post("/members/signup/email")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("우아한테크코스 크루가 아닙니다."))
                .apply(document("member/email/fail/noWooteco"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("가입한 우테코 크루라면 400 반환")
    @Test
    void sendEmail_Exception_Duplicated() {
        EmailRequest emailRequest = new EmailRequest("test@gmail.com");
        doThrow(new TicketUsedException())
                .when(emailService)
                .sendCodeToValidUser(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .when().post("/members/signup/email")
                .then().log().all()
                .assertThat()
                .body("message", equalTo("이미 가입된 크루입니다."))
                .apply(document("member/email/fail/alreadySignUp"))
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
                .assertThat()
                .apply(document("member/verification/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
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
                .apply(document("member/uniqueId/success/true"))
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
                .apply(document("member/uniqueId/success/false"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("닉네임이 중복되지 않으면 true 반환")
    @Test
    void validateUniqueNickname_true() {
        given(memberService.checkUniqueNickname("testNickname"))
                .willReturn(new UniqueResponse(true));

        restDocs
                .when().get("/members/signup/exists?nickname=testNickname")
                .then().log().all()
                .assertThat()
                .body("unique", equalTo(true))
                .apply(document("member/uniqueNickname/success/true"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("닉네임이 중복되면 false 반환")
    @Test
    void validateUniqueNickname_false() {
        given(memberService.checkUniqueNickname("testNickname"))
                .willReturn(new UniqueResponse(false));

        restDocs
                .when().get("/members/signup/exists?nickname=testNickname")
                .then().log().all()
                .assertThat()
                .body("unique", equalTo(false))
                .apply(document("member/uniqueNickname/success/false"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("회원가입을 하면 201 반환")
    @Test
    void signUp() {
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "username", "nickname", "a1b1c1",
                "password1!", "password1!");

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .assertThat()
                .apply(document("member/signup/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("회원가입에서 비밀번호 확인을 잘못 입력할 시 400 반환")
    @Test
    void signUp_Exception_PasswordDifferent() {
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
                .assertThat()
                .apply(document("member/signup/fail/passwordDifferent"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("회원가입에서 우아한테크코스 회원이 아닐 경우 400 반환")
    @Test
    void signUp_Exception_NoWooteco() {
        SignupRequest signupRequest = new SignupRequest("noWooteco@gmail.com", "username", "nickname", "a1b1c1",
                "password1!", "password1!");

        doThrow(new NotWootecoMemberException())
                .when(memberService)
                .signUp(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .assertThat()
                .apply(document("member/signup/fail/noWooteco"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("회원가입에서 이미 가입한 회원일 경우 400 반환")
    @Test
    void signUp_Exception_AlreadySignUp() {
        SignupRequest signupRequest = new SignupRequest("alreadySignUp@gmail.com", "username", "nickname", "a1b1c1",
                "password1!", "password1!");

        doThrow(new TicketUsedException())
                .when(memberService)
                .signUp(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .assertThat()
                .apply(document("member/signup/fail/alreadySignUp"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("회원가입에서 인증번호가 틀렸을 경우 400 반환")
    @Test
    void signUp_Exception_InvalidAuthCode() {
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "username", "nickname", "NoCode",
                "password1!", "password1!");

        doThrow(new InvalidAuthCodeException())
                .when(memberService)
                .signUp(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .assertThat()
                .apply(document("member/signup/fail/invalidAuthCode"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("회원가입에서 이미 존재하는 아이디일 경우 400 반환")
    @Test
    void signUp_Exception_DuplicateUsername() {
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "AlreadyUsername", "nickname", "a1b1c1",
                "password1!", "password1!");

        doThrow(new InvalidSignupFlowException())
                .when(memberService)
                .signUp(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()
                .assertThat()
                .apply(document("member/signup/fail/duplicateUsername"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("회원가입에서 이미 존재하는 닉네임일 경우 400 반환")
    @Test
    void signUp_Exception_DuplicateNickname() {
        SignupRequest signupRequest = new SignupRequest("alreadySignUp@gmail.com", "username", "AlreadyNick", "a1b1c1",
                "password1!", "password1!");

        doThrow(new InvalidSignupFlowException())
                .when(memberService)
                .signUp(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signupRequest)
                .when().post("/members/signup")
                .then().log().all()

                .apply(document("member/signup/fail/duplicateUsername"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("닉네임 조회 시 200 반환")
    @Test
    void findNickname() {
        doReturn(new NicknameResponse("chrisNickname"))
                .when(memberService)
                .findNickname(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .when().get("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/find/nickname/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("닉네임 변경 시 204 반환")
    @Test
    void editNickname() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("chrisNick2");
        doNothing()
                .when(memberService)
                .editNickname(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(nicknameUpdateRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("닉네임 변경 시 이미 있는 닉네임이면 400 반환")
    @Test
    void editNickname_Exception_Duplicate() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("hunchNickname");
        doThrow(new DuplicateNicknameException())
                .when(memberService)
                .editNickname(refEq(nicknameUpdateRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(nicknameUpdateRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/fail/duplicate"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("닉네임 변경 시 잘못된 형식이면 400 반환")
    @Test
    void editNickname_Exception_InvalidFormat() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("");
        doThrow(new InvalidNicknameException())
                .when(memberService)
                .editNickname(refEq(nicknameUpdateRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(nicknameUpdateRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/fail/invalidFormat"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
