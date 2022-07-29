package com.wooteco.sokdak.support.token;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private TokenManager tokenManager;

    @DisplayName("토큰매니저가 payload를 가져온다")
    @Test
    void getPayload() {
        String token = tokenManager.createToken(new AuthInfo(1L));
        assertThat(tokenManager.getPayload(token)).isEqualTo("1");
    }
}
