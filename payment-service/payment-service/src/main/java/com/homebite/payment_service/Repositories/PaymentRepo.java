package com.homebite.payment_service.Repositories;

import com.homebite.payment_service.Entitiy.Payment;
import com.homebite.payment_service.Enum.PaymentMethod;
import com.homebite.payment_service.Enum.PaymentStatus;
import com.homebite.payment_service.Enum.SettlementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    List<Payment> findByPaymentMethodAndPaymentStatusAndSettlementStatus(PaymentMethod method, PaymentStatus status, SettlementStatus settlementStatus);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
