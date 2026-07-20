package com.homebite.menu_services.Client;

import com.homebite.menu_services.Config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "provider-service", url = "http://localhost:8085", configuration = FeignClientConfig.class)
public interface ProviderClient {


    @GetMapping("/{id}/internal-info")
    Map<String, Object> getProviderInternalInfo(
            @PathVariable("id") long providerId,
            @RequestHeader(value = "Authorization",required = false) String token,
            @RequestHeader(value = "X-User-Email",required = false) String email
    );

    @GetMapping("/me")
    Long getLoggedInProviderId(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Email") String email
    );
}