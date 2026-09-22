package com.homebite.payment_service.Entitiy;

import com.homebite.payment_service.Enum.SettlementStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "provider_settlements", uniqueConstraints = @UniqueConstraint(columnNames = "paymentId"))
public class ProviderSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID settlementId;

    @Column(nullable = false)
    private String sourceType;
    @Column(nullable = false)
    private String sourceId;
    @Column(nullable = false, unique = true)
    private UUID paymentId;
    @Column(nullable = false)
    private String providerId;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal grossAmount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal commission;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal netAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status;
    private String razorpayTransferId;
    @Column(nullable = false)
    private Instant createdAt;
    private Instant settledAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = SettlementStatus.PENDING;
    }
}
