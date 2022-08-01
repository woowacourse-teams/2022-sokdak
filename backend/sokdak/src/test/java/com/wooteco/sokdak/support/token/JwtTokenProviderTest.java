package com.wooteco.sokdak.support.token;

import static com.wooteco.sokdak.member.domain.RoleType.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.RoleType;
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
        String token = tokenManager.createAccessToken(new AuthInfo(1L, USER.getName(), "nickname"));
        assertThat(tokenManager.getParsedClaims(token).getId()).isEqualTo(1);
        assertThat(tokenManager.getParsedClaims(token).getRole()).isEqualTo("USER");
        assertThat(tokenManager.getParsedClaims(token).getNickname()).isEqualTo("nickname");
    }
}
