package com.homebite.payment_service.DTOs.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentVerificationDTO {
    private String razorpayOrderId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentLinkStatus;
    @NotBlank
    private String razorpayPaymentId;
    @NotBlank
    private String razorpaySignature;
}
