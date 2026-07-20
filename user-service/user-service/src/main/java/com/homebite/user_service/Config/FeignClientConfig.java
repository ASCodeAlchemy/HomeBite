package com.homebite.user_service.Config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                    String authHeader = request.getHeader("Authorization");
                    String emailHeader = request.getHeader("X-User-Email");

                    if (authHeader != null) {
                        template.header("Authorization", authHeader);
                    }

                    if (emailHeader != null) {
                        template.header("X-User-Email", emailHeader);
                        System.out.println("MENU-SERVICE FEIGN DEBUG: Forwarded X-User-Email -> " + emailHeader);
                    } else {
                        System.out.println("MENU-SERVICE FEIGN DEBUG: Warning! X-User-Email missing from current request context.");
                    }
                }
            }
        };
    }
}
