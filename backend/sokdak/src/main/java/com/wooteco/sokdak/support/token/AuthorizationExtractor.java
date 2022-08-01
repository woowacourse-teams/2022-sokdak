package com.wooteco.sokdak.support.token;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationExtractor {

    public static final String BEARER_TYPE = "Bearer";

    private AuthorizationExtractor() {
    }

    public static String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        return extract(headers);
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Refresh-Token");
        return extract(headers);
    }

    private static String extract(Enumeration<String> headers) {
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                return value.substring(BEARER_TYPE.length()).trim();
            }
        }
        return null;
    }
}
