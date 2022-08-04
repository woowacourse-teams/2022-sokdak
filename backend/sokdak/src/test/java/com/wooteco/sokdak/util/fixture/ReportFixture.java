package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import java.util.List;

public class ReportFixture {

    public static List<String> getTokensForReport() {
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
}
