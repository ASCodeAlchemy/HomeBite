package com.homebite.order_service.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceOrderRequest {
    @NotBlank
    private String providerId;
    @NotBlank
    private String itemTitle;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal price;
    @NotNull
    @Min(1)
    private Integer quantity = 1;
    @DecimalMin(value = "0.00")
    private BigDecimal deliveryFee = BigDecimal.ZERO;
    @DecimalMin(value = "0.00")
    private BigDecimal discount = BigDecimal.ZERO;
}
