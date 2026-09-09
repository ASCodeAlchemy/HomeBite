package com.homebite.payment_service.DTOs.RequestDTO;


import com.homebite.payment_service.Enum.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentsDTO {
    @NotBlank
    private String orderId;
    @NotNull
    private PaymentMethod paymentMethod;
}
