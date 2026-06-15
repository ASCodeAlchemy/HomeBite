package com.homebite.notification_service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

private final EmailService emailService;
private final TemplateEngine templateEngine;
private final ResourceLoader resourceLoader;
private final ObjectMapper objectMapper = new ObjectMapper();

private static final String GROUP_ID= "notification-v1";



    @KafkaListener(topics = "auth-events", groupId = GROUP_ID)
    public void consumeAuthEvent(ConsumerRecord<String, Object> record){

        Map<String,Object> event =normalizePayload(record.value());
        if (event == null || event.isEmpty()) {
            System.out.println("Received empty or unparseable notification event, raw=" + record.value());
            return;
        }

        String type = String.valueOf(event.get("type"));
        String email = String.valueOf(event.get("email"));
        String username = String.valueOf(event.get("username"));
        String otp = String.valueOf(event.get("otp"));

        if ("REGISTER_OTP".equals(type)) {
            emailService.sendHtmlEmail(email, "Verify Your Account", templateEngine.getRegisterOtpTemplate(email,otp));
        }

        if("REGISTER_SUCCESS".equals(type)){
            emailService.sendHtmlEmail(email,"Welcome Onboard of HomeBite", templateEngine.getWelcomeTemplate(email));
        }



    }

    private Map<String, Object> normalizePayload(Object value) {
        if (value == null) return Collections.emptyMap();
        try {
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            }
            if (value instanceof String) {
                String s = (String) value;
                return objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {});
            }
            if (value instanceof byte[]) {
                String s = new String((byte[]) value, StandardCharsets.UTF_8);
                return objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {});
            }
            return objectMapper.convertValue(value, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            System.err.println("Failed to normalize Kafka payload to Map: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
}
