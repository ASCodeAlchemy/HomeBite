package com.homebite.payment_service.Services;

import com.homebite.payment_service.DTOs.OrderDetailsDTO;
import com.homebite.payment_service.DTOs.RequestDTO.PaymentVerificationDTO;
import com.homebite.payment_service.DTOs.RequestDTO.PaymentsDTO;
import com.homebite.payment_service.DTOs.ResponseDTO.PaymentResponseDTO;
import com.homebite.payment_service.Entitiy.Payment;
import com.homebite.payment_service.Enum.PaymentMethod;
import com.homebite.payment_service.Enum.PaymentStatus;
import com.homebite.payment_service.Repositories.PaymentRepo;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestClient orderClient;
    private final String razorpayKeyId;
    private final String razorpayKeySecret;

    public PaymentService(PaymentRepo paymentRepo,
                          KafkaTemplate<String, Object> kafkaTemplate,
                          RestClient.Builder restClientBuilder,
                          @Value("${order-service.url}") String orderServiceUrl,
                          @Value("${razorpay.key-id:}") String razorpayKeyId,
                          @Value("${razorpay.key-secret:}") String razorpayKeySecret) {
        this.paymentRepo = paymentRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.orderClient = restClientBuilder.baseUrl(orderServiceUrl).build();
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
    }

    @Transactional
    public PaymentResponseDTO createPayment(PaymentsDTO request, String userEmail, String gatewaySecret) {
        OrderDetailsDTO order = getOrder(request.getOrderId(), userEmail, gatewaySecret);
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setCustomerId(order.getUserId());
        payment.setProviderId(order.getProviderId());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment = paymentRepo.save(payment);

        if (request.getPaymentMethod() == PaymentMethod.Online_Payment) {
            ensureRazorpayConfigured();
            try {
                RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
                JSONObject options = new JSONObject();
                options.put("amount", amountInPaise(payment.getAmount()));
                options.put("currency", "INR");
                options.put("receipt", "homebite_" + payment.getPaymentId());
                com.razorpay.Order razorpayOrder = razorpay.orders.create(options);
                payment.setRazorpayOrderId(razorpayOrder.get("id").toString());
                payment = paymentRepo.save(payment);
            } catch (Exception exception) {
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Unable to create the online payment order");
                paymentRepo.save(payment);
                throw new IllegalStateException("Unable to create the online payment order", exception);
            }
        }
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponseDTO verifyOnlinePayment(UUID paymentId, PaymentVerificationDTO request, String userEmail) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (!payment.getCustomerId().equals(userEmail)) {
            throw new IllegalArgumentException("You cannot verify this payment");
        }
        if (payment.getPaymentMethod() != PaymentMethod.Online_Payment) {
            throw new IllegalArgumentException("Only online payments require verification");
        }
        if (!request.getRazorpayOrderId().equals(payment.getRazorpayOrderId())) {
            throw new IllegalArgumentException("Payment order does not match");
        }
        ensureRazorpayConfigured();
        if (!isValidSignature(request)) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment signature verification failed");
            return toResponse(paymentRepo.save(payment));
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setTransactionId(request.getRazorpayPaymentId());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        Payment savedPayment = paymentRepo.save(payment);
        publishPaymentEvent(savedPayment);
        return toResponse(savedPayment);
    }

    public PaymentResponseDTO getPayment(UUID paymentId, String userEmail) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (!payment.getCustomerId().equals(userEmail)) {
            throw new IllegalArgumentException("You cannot view this payment");
        }
        return toResponse(payment);
    }

    private OrderDetailsDTO getOrder(String orderId, String userEmail, String gatewaySecret) {
        try {
            OrderDetailsDTO order = orderClient.get()
                    .uri("/orders/{orderId}", orderId)
                    .header("X-User-Email", userEmail)
                    .header("X-Gateway-Secret", gatewaySecret)
                    .retrieve()
                    .body(OrderDetailsDTO.class);
            if (order == null || order.getTotalAmount() == null) {
                throw new IllegalArgumentException("Order details are unavailable");
            }
            return order;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve the selected order", exception);
        }
    }

    private int amountInPaise(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    private void ensureRazorpayConfigured() {
        if (razorpayKeyId.isBlank() || razorpayKeySecret.isBlank()) {
            throw new IllegalStateException("Online payments are not configured");
        }
    }

    private boolean isValidSignature(PaymentVerificationDTO request) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(razorpayKeySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] expected = mac.doFinal((request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId()).getBytes(StandardCharsets.UTF_8));
            byte[] received = HexFormat.of().parseHex(request.getRazorpaySignature());
            return MessageDigest.isEqual(expected, received);
        } catch (Exception exception) {
            return false;
        }
    }

    private void publishPaymentEvent(Payment payment) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", "PAYMENT_COMPLETED");
        event.put("paymentId", payment.getPaymentId().toString());
        event.put("orderId", payment.getOrderId());
        kafkaTemplate.send("payment-events", payment.getOrderId(), event);
    }

    private PaymentResponseDTO toResponse(Payment payment) {
        return new PaymentResponseDTO(
                payment.getPaymentId(), payment.getOrderId(), payment.getCustomerId(), payment.getProviderId(),
                payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentStatus(), payment.getRazorpayOrderId(),
                payment.getRazorpayPaymentId(), payment.getTransactionId(), payment.getPaymentTime(), payment.getFailureReason());
    }
}
