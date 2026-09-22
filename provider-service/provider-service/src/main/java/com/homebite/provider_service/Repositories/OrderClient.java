package com.homebite.provider_service.Repositories;

import com.homebite.provider_service.Config.FeignConfig;
import com.homebite.provider_service.DTOs.RequestDTO.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8083", configuration = FeignConfig.class)
public interface OrderClient {


    @GetMapping("/orders/myOrders")
    List<OrderDTO> findOrderByProvider();
}
