package com.homebite.user_service.Service;


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

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        redisTemplate.opsForValue().set("otp:" + otp, email.toLowerCase(), Duration.ofMinutes(OTP_EXPIRATION_MINUTES));

        return otp;
    }

    public String getEmailByOtp(String otp) {
        return redisTemplate.opsForValue().get("otp:" + otp);
    }

    public String verifyOtpAndGetEmail(String otp) {
        String email = getEmailByOtp(otp);
        if (email == null) return null;

        redisTemplate.delete("otp:" + otp);
        return email;
    }

    public void removeOtp(String otp) {
        redisTemplate.delete("otp:" + otp);
    }






}
