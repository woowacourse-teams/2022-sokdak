package com.wooteco.sokdak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,DELETE,TRACE,OPTIONS,PATCH,PUT";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000",
                        "http://sokdaksokdak.com",
                        "http://www.sokdaksokdak.com",
                        "https://sokdaksokdak.com",
                        "https://www.sokdaksokdak.com",
                        "http://dev.sokdaksokdak.com",
                        "https://dev.sokdaksokdak.com")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///home/ubuntu/images/");
    }
}
