package com.homebite.order_service.Enums;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAYMENT_CONFIRMED,
    ACCEPTED_BY_PROVIDER,
    PREPARING,
    DISPATCHED,
    DELIVERED,
    CANCELLED
}
