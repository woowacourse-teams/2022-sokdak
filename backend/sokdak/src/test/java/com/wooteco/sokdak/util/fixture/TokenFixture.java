package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenFixture {

    private static final Map<String, String> TOKENS = new ConcurrentHashMap<>();

    public static List<String> getTokens() {
        String chris = getToken("chris");
        String josh = getToken("josh");
        String thor = getToken("thor");
        String hunch = getToken("hunch");
        String east = getToken("east");
        return List.of(chris, josh, thor, hunch, east);
    }

    public static String getChrisToken() {
        return getToken("chris");
    }
    public static String getToken(String username) {
        return TOKENS.computeIfAbsent(username, ignored -> {
            LoginRequest loginRequest = new LoginRequest(username, "Abcd123!@");
            return httpPost(loginRequest, "/login").header(AUTHORIZATION);
        });
    }
}
