package com.wooteco.sokdak.support;

import com.wooteco.sokdak.support.token.AuthorizationExtractor;
import com.wooteco.sokdak.support.token.InvalidTokenException;
import com.wooteco.sokdak.support.token.TokenManager;
import com.wooteco.sokdak.support.token.TokenNotFoundException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isGetMethodWithPostsUri(request) || isGetMethodWithBoardsUri(request)) {
            return true;
        }
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        validateExistHeader(request);
        String token = AuthorizationExtractor.extractAccessToken(request);
        validateToken(token);
        return true;
    }

    private boolean isGetMethodWithPostsUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/posts") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithBoardsUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/boards") && request.getMethod().equalsIgnoreCase("GET");
    }

    private void validateExistHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorizationHeader)) {
            throw new TokenNotFoundException();
        }
    }

    private void validateToken(String token) {
        if (!tokenManager.isValid(token)) {
            throw new InvalidTokenException();
        }
    }
}
