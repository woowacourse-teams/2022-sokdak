package com.wooteco.sokdak.util.fixture;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;

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
    public static final AuthInfo AUTH_INFO = new AuthInfo(1L);
}
