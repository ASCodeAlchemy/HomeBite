package com.homebite.provider_service.Config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return template -> {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes)
                            RequestContextHolder
                                    .getRequestAttributes();

            if (attributes == null) {
                return;
            }

            HttpServletRequest request =
                    attributes.getRequest();

            // Authorization
            String authHeader =
                    request.getHeader("Authorization");

            if (authHeader != null) {

                template.header(
                        "Authorization",
                        authHeader
                );
            }

            // User email
            String emailHeader =
                    request.getHeader("X-User-Email");

            if (emailHeader != null) {

                template.header(
                        "X-User-Email",
                        emailHeader
                );
            }

            // Provider ID
            String providerIdHeader =
                    request.getHeader("X-Provider-Id");

            if (providerIdHeader != null) {

                template.header(
                        "X-Provider-Id",
                        providerIdHeader
                );
            }

            // Gateway secret
            String gatewaySecret =
                    request.getHeader("X-Gateway-Secret");

            if (gatewaySecret != null) {

                template.header(
                        "X-Gateway-Secret",
                        gatewaySecret
                );
            }
        };
    }
}