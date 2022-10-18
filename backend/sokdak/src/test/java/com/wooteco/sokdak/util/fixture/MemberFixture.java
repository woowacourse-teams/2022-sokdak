package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.domain.encryptor.Encryptor;
import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import java.util.List;

public class MemberFixture {

    public static final EncryptorI ENCRYPTOR = new Encryptor();
    public static final String VALID_USERNAME = "chris";
    public static final String VALID_PASSWORD = "Abcd123!@";
    public static final String VALID_NICKNAME = "testNickname";
    public static final String APPLICANT_USERNAME = "applicant";

    public static final LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);
    public static final LoginRequest APPLICANT_LOGIN_REQUEST = new LoginRequest(APPLICANT_USERNAME, VALID_PASSWORD);
    public static final LoginRequest INVALID_LOGIN_REQUEST = new LoginRequest("invalidUsername", "invalidPassword1!");

    public static final Long CHRIS_ID = 3L;

    public static List<Member> getMembersForReport() {
        EncryptorI encryptor = ENCRYPTOR;
        return List.of(
                Member.builder().id(1L).username(Username.of(encryptor, "chris")).password(
                        Password.of(encryptor, "Abcd123!@")).nickname(new Nickname("chrisNickname")).build(),
                Member.builder().id(2L).username(Username.of(encryptor, "josh"))
                        .password(Password.of(encryptor, "Abcd123!@")).nickname(new Nickname("joshNickname")).build(),
                Member.builder().id(3L).username(Username.of(encryptor, "thor"))
                        .password(Password.of(encryptor, "Abcd123!@")).nickname(new Nickname("thorNickname")).build(),
                Member.builder().id(4L).username(Username.of(encryptor, "hunch"))
                        .password(Password.of(encryptor, "Abcd123!@")).nickname(new Nickname("hunchNickname")).build(),
                Member.builder().id(5L).username(Username.of(encryptor, "east"))
                        .password(Password.of(encryptor, "Abcd123!@")).nickname(new Nickname("eastNickname")).build()
        );
    }

    public static List<String> getTokens() {
        String token1 = getToken("chris");
        String token2 = getToken("josh");
        String token3 = getToken("thor");
        String token4 = getToken("hunch");
        String token5 = getToken("east");
        return List.of(token1, token2, token3, token4, token5);
    }

    public static String getChrisToken() {
        return getToken("chris");
    }

    public static String getApplicantToken() {
        return getToken("applicant");
    }

    public static String getToken(String username) {
        LoginRequest loginRequest = new LoginRequest(username, "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
