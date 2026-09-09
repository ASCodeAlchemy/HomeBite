package com.homebite.payment_service.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailsDTO {
    private String orderId;
    private String userId;
    private String providerId;
    private BigDecimal totalAmount;
}
