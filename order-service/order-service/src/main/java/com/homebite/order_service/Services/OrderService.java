package com.homebite.order_service.Services;

import com.homebite.order_service.DTOs.TiffinDetailsDTO;
import com.homebite.order_service.Entity.Order;
import com.homebite.order_service.Entity.OrderItem;
import com.homebite.order_service.Enums.OrderStatus;
import com.homebite.order_service.Repositories.OrderRepo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TiffinClient tiffinClient;

    public OrderService(OrderRepo orderRepo, KafkaTemplate<String, Object> kafkaTemplate, TiffinClient tiffinClient) {
        this.orderRepo = orderRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.tiffinClient = tiffinClient;
    }

    @Transactional
    public Order placeOrder(String userId, Long tiffinId, Integer quantity, BigDecimal deliveryFee, BigDecimal discount) {
        TiffinDetailsDTO tiffin = tiffinClient.getTiffin(tiffinId);
        BigDecimal subtotal = tiffin.getPrice().multiply(BigDecimal.valueOf(quantity));
        deliveryFee = deliveryFee == null ? BigDecimal.ZERO : deliveryFee;
        discount = discount == null ? BigDecimal.ZERO : discount;
        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);
        if (total.signum() < 0) {
            throw new IllegalArgumentException("Discount cannot exceed subtotal plus delivery fee");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setProviderId(tiffin.getProviderId().toString());
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);
        order.setDiscount(discount);
        order.setTotalAmount(total);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItemId(tiffin.getTiffinId().toString());
        orderItem.setItemTitle(tiffin.getTiffinName());
        orderItem.setPrice(tiffin.getPrice());
        orderItem.setQuantity(quantity);
        order.getItems().add(orderItem);

        Order savedOrder = orderRepo.save(order);
        publishOrderEvent(savedOrder, "ORDER_CREATED");
        return savedOrder;
    }

    public List<Order> getOrdersForUser(String userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> getOrdersForProvider(String providerId) {
        return orderRepo.findByProviderIdOrderByCreatedAtDesc(providerId);
    }

    public Order getOrder(String orderId, String userId) {
        Order order = requiredOrder(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You cannot view this order");
        }
        return order;
    }

    @Transactional
    public Order updateStatus(String orderId, String providerId, OrderStatus targetStatus) {
        Order order = requiredOrder(orderId);
        if (!order.getProviderId().equals(providerId)) {
            throw new IllegalArgumentException("Only the order's provider can update its status");
        }
        if (!isValidTransition(order.getStatus(), targetStatus)) {
            throw new IllegalArgumentException("Invalid order status transition from " + order.getStatus() + " to " + targetStatus);
        }
        order.setStatus(targetStatus);
        Order savedOrder = orderRepo.save(order);
        publishOrderEvent(savedOrder, "ORDER_STATUS_CHANGED");
        return savedOrder;
    }

    @Transactional
    public void markPaymentCompleted(String orderId) {
        Order order = requiredOrder(orderId);
        if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
            order.setStatus(OrderStatus.PAYMENT_CONFIRMED);
            Order savedOrder = orderRepo.save(order);
            publishOrderEvent(savedOrder, "PAYMENT_COMPLETED");
        }
    }

    private Order requiredOrder(String orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus target) {
        return switch (current) {
            case PAYMENT_PENDING -> target == OrderStatus.PAYMENT_CONFIRMED || target == OrderStatus.ACCEPTED_BY_PROVIDER || target == OrderStatus.CANCELLED;
            case PAYMENT_CONFIRMED -> target == OrderStatus.ACCEPTED_BY_PROVIDER || target == OrderStatus.CANCELLED;
            case ACCEPTED_BY_PROVIDER -> target == OrderStatus.PREPARING || target == OrderStatus.CANCELLED;
            case PREPARING -> target == OrderStatus.DISPATCHED || target == OrderStatus.CANCELLED;
            case DISPATCHED -> target == OrderStatus.DELIVERED;
            default -> false;
        };
    }

    private void publishOrderEvent(Order order, String type) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", type);
        event.put("orderId", order.getOrderId());
        event.put("userId", order.getUserId());
        event.put("providerId", order.getProviderId());
        event.put("status", order.getStatus().name());
        kafkaTemplate.send("order-events", order.getOrderId(), event);
    }
}
