package com.wooteco.sokdak.member.acceptance;

import static com.wooteco.sokdak.util.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.HttpMethodFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.wooteco.sokdak.auth.domain.Ticket;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.service.AuthCodeGenerator;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.repository.TicketRepository;
import com.wooteco.sokdak.member.service.EmailSenderImpl;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("회원가입 관련 인수테스트")
class MemberAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "rerub0831@gmail.com";
    private static final String AUTH_CODE = "123456";
    private static final String USERNAME = "hunch0831";
    private static final String NICKNAME = "헌치";
    private static final String PASSWORD = "abcde123@";

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private Encryptor encryptor;
    @MockBean
    private AuthCodeGenerator authCodeGenerator;
    @MockBean
    private EmailSenderImpl sender;

    @BeforeEach
    void setUpMockAndTicket() {
        Mockito.when(authCodeGenerator.generate(6)).thenReturn(AUTH_CODE);
        Mockito.spy(sender);

        Ticket ticket = Ticket.builder()
                .serialNumber(encryptor.encrypt(EMAIL))
                .used(false)
                .build();
        ticketRepository.save(ticket);
    }

    @DisplayName("정상적인 절차로 회원가입을 할 수 있다.")
    @Test
    void signUp() {
        //given
        VerificationRequest verificationRequest = new VerificationRequest(EMAIL, AUTH_CODE);
        SignupRequest signupRequest = new SignupRequest(EMAIL, USERNAME, NICKNAME, AUTH_CODE,
                PASSWORD, PASSWORD);
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);

        //when
        getEmailAuthCodeResponse(EMAIL);
        getVerificationResponse(verificationRequest);
        getValidateUsernameResponse(USERNAME);
        getValidateNicknameResponse(NICKNAME);

        //then

        assertAll(
                () -> assertThat(getSignupResponse(signupRequest).statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(getLoginResponse(loginRequest).statusCode()).isEqualTo(OK.value())
        );
    }

    @DisplayName("잘못된 인증번호로 회원가입할 수 없다.")
    @Test
    void singUp_Exception_WrongAuthCode() {
        //given
        String wrongAuthCode = "111111";
        VerificationRequest verificationRequest = new VerificationRequest(EMAIL, wrongAuthCode);
        SignupRequest signupRequest = new SignupRequest(EMAIL, USERNAME, NICKNAME, wrongAuthCode,
                PASSWORD, PASSWORD);
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);

        //when
        getEmailAuthCodeResponse(EMAIL);
        getVerificationResponse(verificationRequest);
        getValidateUsernameResponse(USERNAME);
        getValidateNicknameResponse(NICKNAME);

        //then

        assertAll(
                () -> assertThat(getSignupResponse(signupRequest).statusCode()).isEqualTo(BAD_REQUEST.value()),
                () -> assertThat(getLoginResponse(loginRequest).statusCode()).isEqualTo(BAD_REQUEST.value())
        );
    }

    @DisplayName("우테코 크루가 아닌 이메일로 회원가입할 수 없다.")
    @Test
    void singUp_Exception_WrongEmail() {
        //given
        String wrongEmail = "fakecrew@gmail.com";
        VerificationRequest verificationRequest = new VerificationRequest(wrongEmail, AUTH_CODE);
        SignupRequest signupRequest = new SignupRequest(wrongEmail, USERNAME, NICKNAME, AUTH_CODE,
                PASSWORD, PASSWORD);
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);

        //when
        assertThat(getEmailAuthCodeResponse(wrongEmail).statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(getVerificationResponse(verificationRequest).statusCode()).isEqualTo(BAD_REQUEST.value());
        getValidateUsernameResponse(USERNAME);
        getValidateNicknameResponse(NICKNAME);

        //then
        assertAll(
                () -> assertThat(getSignupResponse(signupRequest).statusCode()).isEqualTo(BAD_REQUEST.value()),
                () -> assertThat(getLoginResponse(loginRequest).statusCode()).isEqualTo(BAD_REQUEST.value())
        );
    }

    @DisplayName("이미 가입한 회원은 회원가입할 수 없다.")
    @Test
    void singUp_Exception_AlreadySigned() {
        //given
        VerificationRequest verificationRequest = new VerificationRequest(EMAIL, AUTH_CODE);
        SignupRequest signupRequest = new SignupRequest(EMAIL, USERNAME, NICKNAME, AUTH_CODE,
                PASSWORD, PASSWORD);
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);
        getEmailAuthCodeResponse(EMAIL);
        getVerificationResponse(verificationRequest);
        getSignupResponse(signupRequest);

        //when
        getEmailAuthCodeResponse(EMAIL);
        getVerificationResponse(verificationRequest);
        getValidateUsernameResponse(USERNAME);
        getValidateNicknameResponse(NICKNAME);

        //then
        assertThat(getSignupResponse(signupRequest).statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(getLoginResponse(loginRequest).statusCode()).isEqualTo(OK.value());
    }

    private ExtractableResponse<Response> getSignupResponse(SignupRequest signupRequest) {
        return httpPost(signupRequest, "/members/signup");
    }

    private ExtractableResponse<Response> getValidateNicknameResponse(String nickname) {
        return httpGet("/members/signup/exists?nickname=" + nickname);
    }

    private ExtractableResponse<Response> getValidateUsernameResponse(String username) {
        return httpGet("/members/signup/exists?username=" + username);
    }

    private ExtractableResponse<Response> getVerificationResponse(VerificationRequest verificationRequest) {
        return httpPost(verificationRequest, "/members/signup/email/verification");
    }

    private ExtractableResponse<Response> getEmailAuthCodeResponse(String email) {
        return httpPost(new EmailRequest(email), "/members/signup/email");
    }

    private ExtractableResponse<Response> getLoginResponse(LoginRequest loginRequest) {
        return httpPost(loginRequest, "/login");
    }
}
