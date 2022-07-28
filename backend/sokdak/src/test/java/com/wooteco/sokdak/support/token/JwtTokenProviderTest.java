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

    @DisplayName("access token을 parsing 하여 정보를 뽑아내는 기능")
    @Test
    void getPayload() {
        String token = tokenManager.createAccessToken(new AuthInfo(1L));
        assertThat(tokenManager.getPayload(token)).isEqualTo("1");
    }
}
