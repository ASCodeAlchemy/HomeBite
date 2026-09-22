package com.homebite.payment_service.DTOs.RequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SubscriptionPaymentRequest {
    @NotNull
    private UUID subscriptionId;
}
