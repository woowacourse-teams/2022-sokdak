package com.wooteco.sokdak.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (isGetMethodWithPostsUri(request)) {
            return true;
        }
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private boolean isGetMethodWithPostsUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/posts") && request.getMethod().equalsIgnoreCase("GET");
    }
}
