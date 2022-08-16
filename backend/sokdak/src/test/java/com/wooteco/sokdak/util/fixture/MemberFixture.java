package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import java.util.List;

public class MemberFixture {

    public static final String VALID_USERNAME = "chris";
    public static final String VALID_PASSWORD = "Abcd123!@";
    public static final String VALID_ENCRYPTED_PASSWORD = "6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e";
    public static final String VALID_NICKNAME = "testNickname";

    public static final String INVALID_USERNAME = "invalidUsername";
    public static final String INVALID_PASSWORD = "invalidPassword1!";

    public static final LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);
    public static final LoginRequest INVALID_LOGIN_REQUEST = new LoginRequest(INVALID_USERNAME, INVALID_PASSWORD);

    public static final String SESSION_ID = "mySessionId";

    public static List<Member> getMembersForReport() {
        return List.of(
                Member.builder().id(1L).username("chris").password("Abcd123!@").nickname("chrisNickname").build(),
                Member.builder().id(2L).username("josh").password("Abcd123!@").nickname("joshNickname").build(),
                Member.builder().id(3L).username("hunch").password("Abcd123!@").nickname("hunchNickname").build(),
                Member.builder().id(4L).username("east").password("Abcd123!@").nickname("eastNickname").build(),
                Member.builder().id(5L).username("thor").password("Abcd123!@").nickname("thorNickname").build()
        );
    }

    public static List<String> getTokensForReport() {
        String token1 = getToken("chris", "Abcd123!@");
        String token2 = getToken("josh", "Abcd123!@");
        String token3 = getToken("thor", "Abcd123!@");
        String token4 = getToken("hunch", "Abcd123!@");
        String token5 = getToken("east", "Abcd123!@");
        return List.of(token1, token2, token3, token4, token5);
    }

    public static List<String> getTokensForLike() {
        String token1 = getToken("chris", "Abcd123!@");
        String token2 = getToken("josh", "Abcd123!@");
        String token3 = getToken("thor", "Abcd123!@");
        String token4 = getToken("hunch", "Abcd123!@");
        String token5 = getToken("east", "Abcd123!@");
        return List.of(token1, token2, token3, token4, token5);
    }

    public static String getToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    public static final AuthInfo AUTH_INFO = new AuthInfo(1L, RoleType.USER.getName(), "chrisNickname");
    public static final AuthInfo AUTH_INFO2 = new AuthInfo(3L, RoleType.USER.getName(), "joshNickname");
}
