package com.homebite.payment_service.Controller;

import com.homebite.payment_service.Client.ProviderClient;
import com.homebite.payment_service.Entitiy.ProviderSettlement;
import com.homebite.payment_service.Services.SettlementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments/settlements")
public class SettlementController {
    private final SettlementService settlementService;
    private final ProviderClient providerClient;
    private final String gatewaySecret;

    public SettlementController(SettlementService settlementService, ProviderClient providerClient,
                                @Value("${gateway.internal-secret}") String gatewaySecret) {
        this.settlementService = settlementService;
        this.providerClient = providerClient;
        this.gatewaySecret = gatewaySecret;
    }

    @GetMapping("/mine")
    public List<ProviderSettlement> mine(@RequestHeader("X-User-Email") String email,
                                         @RequestHeader("X-Gateway-Secret") String requestSecret) {
        if (!gatewaySecret.equals(requestSecret)) throw new IllegalArgumentException("Requests must be made through the API gateway");
        return settlementService.settlementsForProvider(providerClient.providerIdFor(email));
    }
}
