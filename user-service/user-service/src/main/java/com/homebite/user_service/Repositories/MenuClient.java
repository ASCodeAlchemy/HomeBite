package com.homebite.user_service.Repositories;


import com.homebite.user_service.Config.FeignClientConfig;
import com.homebite.user_service.DTOs.RequestDTO.MenuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "menu-service",url = "http://localhost:8086",configuration = FeignClientConfig.class)
public interface MenuClient {


    @GetMapping("/menus/dashboard")
    List<MenuDTO> getDashboardMenus();
}
