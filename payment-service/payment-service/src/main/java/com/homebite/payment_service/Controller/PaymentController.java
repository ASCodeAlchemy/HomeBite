package com.homebite.payment_service.Controller;

import com.homebite.payment_service.DTOs.RequestDTO.PaymentVerificationDTO;
import com.homebite.payment_service.DTOs.RequestDTO.PaymentsDTO;
import com.homebite.payment_service.DTOs.RequestDTO.SubscriptionPaymentRequest;
import com.homebite.payment_service.DTOs.ResponseDTO.PaymentResponseDTO;
import com.homebite.payment_service.Services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final String gatewaySecret;

    public PaymentController(PaymentService paymentService, @Value("${gateway.internal-secret}") String gatewaySecret) {
        this.paymentService = paymentService;
        this.gatewaySecret = gatewaySecret;
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<PaymentResponseDTO> createSubscriptionPayment(@RequestHeader("X-User-Email") String userEmail,
                                                                        @RequestHeader("X-Gateway-Secret") String requestSecret,
                                                                        @Valid @RequestBody SubscriptionPaymentRequest request) {
        requireGateway(requestSecret);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createSubscriptionPayment(request, userEmail));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestHeader("X-User-Email") String userEmail,
                                                             @RequestHeader("X-Gateway-Secret") String requestSecret,
                                                             @Valid @RequestBody PaymentsDTO request) {
        requireGateway(requestSecret);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request, userEmail, requestSecret));
    }

    @PostMapping("/{paymentId}/verify")
    public PaymentResponseDTO verifyPayment(@RequestHeader("X-User-Email") String userEmail,
                                             @RequestHeader("X-Gateway-Secret") String requestSecret,
                                             @PathVariable UUID paymentId,
                                             @Valid @RequestBody PaymentVerificationDTO request) {
        requireGateway(requestSecret);
        return paymentService.verifyOnlinePayment(paymentId, request, userEmail);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponseDTO getPayment(@RequestHeader("X-User-Email") String userEmail,
                                         @RequestHeader("X-Gateway-Secret") String requestSecret,
                                         @PathVariable UUID paymentId) {
        requireGateway(requestSecret);
        return paymentService.getPayment(paymentId, userEmail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleUnavailable(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("message", exception.getMessage()));
    }

    private void requireGateway(String requestSecret) {
        if (!gatewaySecret.equals(requestSecret)) {
            throw new IllegalArgumentException("Requests must be made through the API gateway");
        }
    }
}
