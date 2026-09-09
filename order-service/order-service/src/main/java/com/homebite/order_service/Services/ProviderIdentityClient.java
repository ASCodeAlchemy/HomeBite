package com.homebite.order_service.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProviderIdentityClient {
    private final RestClient providerClient;

    public ProviderIdentityClient(RestClient.Builder restClientBuilder,
                                  @Value("${provider-service.url:http://localhost:8085}") String providerServiceUrl) {
        this.providerClient = restClientBuilder.baseUrl(providerServiceUrl).build();
    }

    public String getProviderId(String email) {
        try {
            Long providerId = providerClient.get()
                    .uri("/me")
                    .header("X-User-Email", email)
                    .retrieve()
                    .body(Long.class);
            if (providerId == null) {
                throw new IllegalArgumentException("Provider account not found");
            }
            return providerId.toString();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Only an authenticated provider can perform this action", exception);
        }
    }
}
