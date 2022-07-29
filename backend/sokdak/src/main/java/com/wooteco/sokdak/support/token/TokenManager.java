package com.wooteco.sokdak.support.token;

import com.wooteco.sokdak.auth.dto.AuthInfo;

public interface TokenManager {

    String createAccessToken(AuthInfo authInfo);

    String createRefreshToken();

    String getPayload(String token);

    boolean isValid(String token);
}
