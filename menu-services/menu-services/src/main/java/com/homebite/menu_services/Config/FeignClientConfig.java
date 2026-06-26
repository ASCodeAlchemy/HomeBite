package com.homebite.menu_services.Config;

import org.springframework.context.annotation.Configuration;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Configuration

public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();


                    if (request.getCookies() != null) {
                        for (Cookie cookie : request.getCookies()) {
                            if ("jwt".equals(cookie.getName())) {
                                // Forward the exact cookie structure down to the Provider Service
                                template.header("Cookie", "jwt=" + cookie.getValue());
                                break;
                            }
                        }
                    }
                }
            }
        };
    }
}
