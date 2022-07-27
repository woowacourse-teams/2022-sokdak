package com.wooteco.sokdak.support.token;

import com.wooteco.sokdak.auth.dto.AuthInfo;

public interface TokenManager {

    String createToken(AuthInfo authInfo);

    String getPayload(String token);

    boolean isValid(String token);
}
