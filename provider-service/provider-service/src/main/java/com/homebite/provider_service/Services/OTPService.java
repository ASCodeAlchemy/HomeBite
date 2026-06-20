package com.homebite.provider_service.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class OTPService {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final int OTP_EXPIRATION_MINUTES = 10;

    private static final String REDIS_KEY_PREFIX = "provider:otp:";

    public String generateOtp(String email) {

        String otp = String.valueOf(
                new Random().nextInt(900000) + 100000
        );

        redisTemplate.opsForValue().set(
                REDIS_KEY_PREFIX + otp,
                email.toLowerCase(),
                Duration.ofMinutes(OTP_EXPIRATION_MINUTES)
        );

        return otp;
    }

    public String verifyOtp(String inputOtp) {
        String redisKey = REDIS_KEY_PREFIX + inputOtp;
        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            return null;
        }

        redisTemplate.delete(redisKey);
        return email;
    }
    public String getEmailByOtp(String otp) {
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + otp);
    }

    public void removeOtp(String email) {
        redisTemplate.delete(getRedisKey(email));
    }

    private String getRedisKey(String email) {
        return REDIS_KEY_PREFIX + email.trim().toLowerCase();
    }
}
