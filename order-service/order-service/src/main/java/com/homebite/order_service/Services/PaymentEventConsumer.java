package com.homebite.order_service.Services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentEventConsumer {
    private final OrderService orderService;

    public PaymentEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-service-payments")
    public void handlePaymentEvent(Map<String, Object> event) {
        if ("PAYMENT_COMPLETED".equals(event.get("type")) && event.get("orderId") != null) {
            orderService.markPaymentCompleted(String.valueOf(event.get("orderId")));
        }
    }
}
