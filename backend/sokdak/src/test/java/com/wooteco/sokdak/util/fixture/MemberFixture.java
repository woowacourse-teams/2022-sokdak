package com.wooteco.sokdak.util.fixture;

import com.wooteco.sokdak.auth.dto.LoginRequest;

public class MemberFixture {

    public static final String VALID_USERNAME = "chris";
    public static final String VALID_PASSWORD = "Abcd123!@";
    public static final String INVALID_USERNAME = "invalidUsername";
    public static final String INVALID_PASSWORD = "invalidPassword1!";

    public static final LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);
    public static final LoginRequest INVALID_LOGIN_REQUEST = new LoginRequest(INVALID_USERNAME, INVALID_PASSWORD);
}
