package com.homebite.payment_service.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ProviderClient {
    private final RestClient providerClient;

    public ProviderClient(RestClient.Builder restClientBuilder,
                          @Value("${provider-service.url}") String providerServiceUrl) {
        this.providerClient = restClientBuilder.baseUrl(providerServiceUrl).build();
    }

    public String providerIdFor(String email) {
        Object response = providerClient.get().uri("/me").header("X-User-Email", email).retrieve().body(Object.class);
        if (response == null) throw new IllegalArgumentException("Provider account not found");
        return response.toString();
    }

    @SuppressWarnings("unchecked")
    public ProviderPayoutDetails payoutDetailsFor(String providerId) {
        Map<String, Object> response = providerClient.get().uri("/{providerId}/internal-info", providerId)
                .retrieve().body(Map.class);
        if (response == null) throw new IllegalArgumentException("Provider payout details are unavailable");
        Object commission = response.get("commissionPercentage");
        BigDecimal percentage = commission == null ? BigDecimal.ZERO : new BigDecimal(commission.toString());
        return new ProviderPayoutDetails(providerId, (String) response.get("razorpayAccountId"), percentage,
                Boolean.TRUE.equals(response.get("payoutOnboarded")));
    }
}
