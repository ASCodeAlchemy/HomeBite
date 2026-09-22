package com.homebite.payment_service.Repositories;

import com.homebite.payment_service.Entitiy.ProviderSettlement;
import com.homebite.payment_service.Enum.SettlementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderSettlementRepo extends JpaRepository<ProviderSettlement, UUID> {
    List<ProviderSettlement> findByProviderIdOrderByCreatedAtDesc(String providerId);
    List<ProviderSettlement> findByStatusAndSourceType(SettlementStatus status, String sourceType);
    Optional<ProviderSettlement> findByPaymentId(UUID paymentId);
}
