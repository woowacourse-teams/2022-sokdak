package com.wooteco.sokdak.util.fixture;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import java.util.List;

public class MemberFixture {

    public static final String VALID_USERNAME = "chris";
    public static final String VALID_PASSWORD = "Abcd123!@";
    public static final String VALID_ENCRYPTED_PASSWORD = "6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e";
    public static final String VALID_NICKNAME = "josh";

    public static final String INVALID_USERNAME = "invalidUsername";
    public static final String INVALID_PASSWORD = "invalidPassword1!";

    public static final LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);
    public static final LoginRequest INVALID_LOGIN_REQUEST = new LoginRequest(INVALID_USERNAME, INVALID_PASSWORD);

    public static final String SESSION_ID = "mySessionId";
    public static final AuthInfo AUTH_INFO = new AuthInfo(1L, RoleType.ADMIN.getName(), "nickname");

    public static List<Member> getMembersForReport() {
        return List.of(
                Member.builder().username("chris").password("Abcd123!@").nickname("chrisNickname").build(),
                Member.builder().username("josh").password("Abcd123!@").nickname("joshNickname").build(),
                Member.builder().username("hunch").password("Abcd123!@").nickname("hunchNickname").build(),
                Member.builder().username("east").password("Abcd123!@").nickname("eastNickname").build(),
                Member.builder().username("thor").password("Abcd123!@").nickname("thorNickname").build()
        );
    }
}
