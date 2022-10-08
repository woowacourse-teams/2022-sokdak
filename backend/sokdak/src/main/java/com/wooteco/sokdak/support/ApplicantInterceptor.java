package com.wooteco.sokdak.support;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.support.token.AuthorizationExtractor;
import com.wooteco.sokdak.support.token.TokenManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class ApplicantInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

    private final TokenManager tokenManager;

    public ApplicantInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("enter@@@@@@@@@@@@@");
        if (isNotApplicantUser(request)) {
            return true;
        }
        if (isGetMethod(request)) {
            return true;
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return false;
    }

    private boolean isGetMethod(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isNotApplicantUser(HttpServletRequest request) {
        AuthInfo authInfo = extractAuthInfo(request);
        System.out.println("@@@@@@@@@@@@@@@");
        System.out.println(authInfo.getRole());
        System.out.println(RoleType.APPLICANT.getName());
        return !RoleType.APPLICANT.getName().equals(authInfo.getRole());
    }


    private AuthInfo extractAuthInfo(HttpServletRequest request) {
        String token = AuthorizationExtractor.extractAccessToken(request);
        return tokenManager.getParsedClaims(token);
    }
}
