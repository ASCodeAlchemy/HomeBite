package com.homebite.provider_service.DTOs.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderOrderDTO {

    private String orderId;
    List<OrderDTO> orders;
}
