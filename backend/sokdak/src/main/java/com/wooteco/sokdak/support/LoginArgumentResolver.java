package com.wooteco.sokdak.support;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthInfoMapper authInfoMapper;

    public LoginArgumentResolver(AuthInfoMapper authInfoMapper) {
        this.authInfoMapper = authInfoMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasAuthInfo = AuthInfo.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasAuthInfo;
    }

    @Override
    public AuthInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return new AuthInfo(null);
        }
        return authInfoMapper.getAuthInfo(session);
    }
}
