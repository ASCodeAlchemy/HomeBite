package com.homebite.menu_services.DTOs.ResponseDTO;

import java.math.BigDecimal;

public record TiffinDetailsDTO(
        Long tiffinId,
        Long providerId,
        String tiffinName,
        BigDecimal price,
        Boolean veg
) {
}
