package com.homebite.payment_service.DTOs.ResponseDTO;

import com.homebite.payment_service.Enum.PaymentMethod;
import com.homebite.payment_service.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

    private UUID paymentId;
    private String orderId;
    private String customerId;
    private String providerId;
    private BigDecimal amount;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private String razorpayOrderId;
    private String razorpayPaymentId;

    private String transactionId;
    private LocalDateTime paymentTime;
    private String failureReason;
}
