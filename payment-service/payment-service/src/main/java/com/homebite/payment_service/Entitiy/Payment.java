package com.homebite.payment_service.Entitiy;


import com.homebite.payment_service.Enum.PaymentMethod;
import com.homebite.payment_service.Enum.PaymentStatus;
import com.homebite.payment_service.Enum.SettlementStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments_details")
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;
    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private String razorpayOrderId;
    private String razorpayPaymentLinkId;
    @Column(length = 2048)
    private String paymentLink;
    private String razorpayPaymentId;
    private String razorpaySignature;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus = SettlementStatus.PENDING;

    @Column(unique = true)
    private String transactionId;
    private LocalDateTime paymentTime;

    @Column(length = 500)
    private String failureReason;

    @PrePersist
    protected void onCreate() {
        this.paymentTime = LocalDateTime.now();
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
    }
}
