package com.homebite.order_service.Services;

import com.homebite.order_service.DTOs.TiffinDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TiffinClient {
    private final RestClient menuClient;
    private final String internalServiceSecret;

    public TiffinClient(RestClient.Builder restClientBuilder,
                        @Value("${menu-service.url}") String menuServiceUrl,
                        @Value("${internal.service-secret}") String internalServiceSecret) {
        this.menuClient = restClientBuilder.baseUrl(menuServiceUrl).build();
        this.internalServiceSecret = internalServiceSecret;
    }

    public TiffinDetailsDTO getTiffin(Long tiffinId) {
        try {
            TiffinDetailsDTO tiffin = menuClient.get()
                    .uri("/internal/tiffins/{tiffinId}", tiffinId)
                    .header("X-Internal-Service-Secret", internalServiceSecret)
                    .retrieve()
                    .body(TiffinDetailsDTO.class);
            if (tiffin == null || tiffin.getPrice() == null || tiffin.getProviderId() == null) {
                throw new IllegalArgumentException("Tiffin details are unavailable");
            }
            return tiffin;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Selected tiffin was not found or is unavailable", exception);
        }
    }
}
