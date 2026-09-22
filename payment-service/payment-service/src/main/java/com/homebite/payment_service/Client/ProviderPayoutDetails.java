package com.homebite.payment_service.Client;

import java.math.BigDecimal;

public record ProviderPayoutDetails(String providerId, String razorpayAccountId,
                                    BigDecimal commissionPercentage, boolean payoutOnboarded) {
}
