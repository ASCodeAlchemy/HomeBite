package com.homebite.order_service.Controllers;

import com.homebite.order_service.DTOs.OrderDTO;
import com.homebite.order_service.DTOs.OrderStatusUpdateRequest;
import com.homebite.order_service.Entity.Order;
import com.homebite.order_service.Services.OrderService;
import com.homebite.order_service.Services.ProviderIdentityClient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final ProviderIdentityClient providerIdentityClient;
    private final String gatewaySecret;


    public OrderController(OrderService orderService,
                           ProviderIdentityClient providerIdentityClient,
                           @Value("${gateway.internal-secret}") String gatewaySecret) {
        this.orderService = orderService;
        this.providerIdentityClient = providerIdentityClient;
        this.gatewaySecret = gatewaySecret;

    }

    @PostMapping("/{tiffinId}")
    public ResponseEntity<Order> placeOrder(@RequestHeader("X-User-Email") String userId,
                                            @RequestHeader("X-Gateway-Secret") String requestSecret,
                                            @PathVariable Long tiffinId,
                                            @RequestParam(defaultValue = "1") @Min(1) Integer quantity,
                                            @RequestParam(defaultValue = "0") @DecimalMin(value = "0.00") BigDecimal deliveryFee,
                                            @RequestParam(defaultValue = "0") @DecimalMin(value = "0.00") BigDecimal discount) {
        requireGateway(requestSecret);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderService.placeOrder(userId, tiffinId, quantity, deliveryFee, discount));
    }

    @GetMapping("/my")
    public List<Order> getMyOrders(@RequestHeader("X-User-Email") String userId,
                                   @RequestHeader("X-Gateway-Secret") String requestSecret) {
        requireGateway(requestSecret);
        return orderService.getOrdersForUser(userId);
    }

    @GetMapping("/recent")
    public List<Order> getRecentOrders(@RequestHeader("X-User-Email") String userId,
                                       @RequestHeader("X-Gateway-Secret") String requestSecret) {
        requireGateway(requestSecret);
        return orderService.getRecentOrdersForUser(userId);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@RequestHeader("X-User-Email") String userId,
                          @RequestHeader("X-Gateway-Secret") String requestSecret,
                          @PathVariable String orderId) {
        requireGateway(requestSecret);
        return orderService.getOrder(orderId, userId);
    }

    @GetMapping("/provider/{providerId}")
    public List<Order> getProviderOrders(@RequestHeader("X-User-Email") String userEmail,
                                         @RequestHeader("X-Gateway-Secret") String requestSecret,
                                         @PathVariable String providerId) {
        requireGateway(requestSecret);
        requireProviderOwnership(userEmail, providerId);
        return orderService.getOrdersForProvider(providerId);
    }

    @PatchMapping("/{orderId}/status")
    public Order updateStatus(@RequestHeader("X-User-Email") String userEmail,
                              @RequestHeader("X-Gateway-Secret") String requestSecret,
                              @PathVariable String orderId,
                              @Valid @RequestBody OrderStatusUpdateRequest request) {
        requireGateway(requestSecret);
        return orderService.updateStatus(orderId, providerIdentityClient.getProviderId(userEmail), request.getStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }

    private void requireGateway(String requestSecret) {
        if (!gatewaySecret.equals(requestSecret)) {
            throw new IllegalArgumentException("Requests must be made through the API gateway");
        }
    }

    private void requireProviderOwnership(String userEmail, String providerId) {
        if (!providerIdentityClient.getProviderId(userEmail).equals(providerId)) {
            throw new IllegalArgumentException("You cannot view another provider's orders");
        }
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderDTO>> findMyOrders(
            @RequestHeader("X-Provider-Id")
            String providerId) {

        List<OrderDTO> orders =
                orderService.findOrdersByProvider(
                        providerId
                );

        return ResponseEntity.ok(orders);
    }
}
