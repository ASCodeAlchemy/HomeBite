package com.homebite.payment_service.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SubscriptionPaymentDetails {
    private UUID subscriptionId;
    private String userEmail;
    private String providerId;
    private BigDecimal amount;
}
