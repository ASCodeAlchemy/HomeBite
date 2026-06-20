package com.homebite.provider_service.Services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebite.provider_service.DTOs.RequestDTO.ProviderDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PendingUserService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int PENDING_USER_TTL_MINUTES = 30;

    public PendingUserService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void savePendingUser(ProviderDTO providerDTO) throws Exception {
        String key = "pendingUser:" + providerDTO.getEmail().toLowerCase();
        String value = objectMapper.writeValueAsString(providerDTO);
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(PENDING_USER_TTL_MINUTES));
    }

    public ProviderDTO getPendingUser(String email) throws Exception {
        String key = "pendingUser:" + email.toLowerCase();
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return objectMapper.readValue(value, ProviderDTO.class);
    }

    public void remove(String email) {
        String key = "pendingUser:" + email.toLowerCase();
        redisTemplate.delete(key);
    }

}
