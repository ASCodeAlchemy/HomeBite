package com.homebite.menu_services.Client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "provider-service")
public interface ProviderClient {

    @GetMapping("/{id}/internal-info")
    Map<String,Object> getProviderInternalInfo(@PathVariable ("id") long providerId);

    @GetMapping("/api/providers/me")
    Long getLoggedInProviderId();
}
