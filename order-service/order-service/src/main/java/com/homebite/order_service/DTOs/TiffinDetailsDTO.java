package com.homebite.order_service.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TiffinDetailsDTO {
    private Long tiffinId;
    private Long providerId;
    private String tiffinName;
    private BigDecimal price;
    private Boolean veg;
}
