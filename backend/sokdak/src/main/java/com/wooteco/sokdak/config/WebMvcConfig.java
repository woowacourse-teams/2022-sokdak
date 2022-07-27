package com.wooteco.sokdak.config;

import com.wooteco.sokdak.support.AuthInterceptor;
import com.wooteco.sokdak.support.token.AuthenticationPrincipalArgumentResolver;
import com.wooteco.sokdak.support.token.TokenManager;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,DELETE,TRACE,OPTIONS,PATCH,PUT";

    private final AuthInterceptor authInterceptor;
    private final TokenManager tokenManager;

    public WebMvcConfig(AuthInterceptor authInterceptor, TokenManager tokenManager) {
        this.authInterceptor = authInterceptor;
        this.tokenManager = tokenManager;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://3.34.1.220", "http://sokdaksokdak.com"
                , "http://www.sokdaksokdak.com", "https://sokdaksokdak.com", "https://www.sokdaksokdak.com")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.LOCATION)
                .exposedHeaders(HttpHeaders.AUTHORIZATION);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/members/signup/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(tokenManager);
    }
}
