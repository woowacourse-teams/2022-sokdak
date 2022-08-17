package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.domain.Member;
import java.util.List;

public class MemberFixture {

    public static final String VALID_USERNAME = "chris";
    public static final String VALID_PASSWORD = "Abcd123!@";
    public static final String VALID_NICKNAME = "testNickname";

    public static final LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);
    public static final LoginRequest INVALID_LOGIN_REQUEST = new LoginRequest("invalidUsername", "invalidPassword1!");

    public static List<Member> getMembersForReport() {
        return List.of(
                Member.builder().id(1L).username("chris").password("Abcd123!@").nickname("chrisNickname").build(),
                Member.builder().id(2L).username("josh").password("Abcd123!@").nickname("joshNickname").build(),
                Member.builder().id(3L).username("hunch").password("Abcd123!@").nickname("hunchNickname").build(),
                Member.builder().id(4L).username("east").password("Abcd123!@").nickname("eastNickname").build(),
                Member.builder().id(5L).username("thor").password("Abcd123!@").nickname("thorNickname").build()
        );
    }

    public static List<String> getFiveTokens() {
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

    public static String getToken(String username) {
        LoginRequest loginRequest = new LoginRequest(username, "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
