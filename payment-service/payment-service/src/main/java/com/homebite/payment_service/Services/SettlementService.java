package com.homebite.payment_service.Services;

import com.homebite.payment_service.Client.ProviderClient;

import com.homebite.payment_service.Client.ProviderPayoutDetails;
import com.homebite.payment_service.Entitiy.Payment;
import com.homebite.payment_service.Entitiy.ProviderSettlement;
import com.homebite.payment_service.Enum.PaymentMethod;
import com.homebite.payment_service.Enum.PaymentStatus;
import com.homebite.payment_service.Enum.SettlementStatus;
import com.homebite.payment_service.Repositories.PaymentRepo;
import com.homebite.payment_service.Repositories.ProviderSettlementRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class SettlementService {
    private final PaymentRepo paymentRepo;
    private final ProviderSettlementRepo settlementRepo;
    private final ProviderClient providerClient;
    private final RestClient razorpayClient;
    private final String razorpayKeyId;
    private final String razorpayKeySecret;

    public SettlementService(PaymentRepo paymentRepo, ProviderSettlementRepo settlementRepo, ProviderClient providerClient,
                             RestClient.Builder restClientBuilder,
                             @Value("${razorpay.key-id:}") String razorpayKeyId,
                             @Value("${razorpay.key-secret:}") String razorpayKeySecret) {
        this.paymentRepo = paymentRepo;
        this.settlementRepo = settlementRepo;
        this.providerClient = providerClient;
        this.razorpayClient = restClientBuilder.baseUrl("https://api.razorpay.com").build();
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
    }

    @Transactional
    public void recordSettlementForCompletedPayment(Payment payment) {
        if (settlementRepo.findByPaymentId(payment.getPaymentId()).isPresent()) return;
        if (isSubscription(payment)) {
            settleInstant(payment);
        } else {
            ProviderSettlement settlement = newSettlement(payment, SettlementStatus.PENDING);
            settlementRepo.save(settlement);
            payment.setSettlementStatus(SettlementStatus.PENDING);
            paymentRepo.save(payment);
        }
    }

    @Transactional
    public void settleInstant(Payment payment) {
        ProviderSettlement settlement = settlementRepo.findByPaymentId(payment.getPaymentId())
                .orElseGet(() -> settlementRepo.save(newSettlement(payment, SettlementStatus.PENDING)));
        requestTransfer(payment, settlement);
    }

    @Transactional
    public void runEndOfDaySettlement() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Payment> payments = paymentRepo.findByPaymentMethodAndPaymentStatusAndSettlementStatus(
                PaymentMethod.Online_Payment, PaymentStatus.COMPLETED, SettlementStatus.PENDING);
        for (Payment payment : payments) {
            if (isSubscription(payment) || payment.getPaymentTime() == null || !payment.getPaymentTime().toLocalDate().equals(yesterday)) continue;
            ProviderSettlement settlement = settlementRepo.findByPaymentId(payment.getPaymentId())
                    .orElseGet(() -> settlementRepo.save(newSettlement(payment, SettlementStatus.PENDING)));
            requestTransfer(payment, settlement);
        }
    }

    public List<ProviderSettlement> settlementsForProvider(String providerId) {
        return settlementRepo.findByProviderIdOrderByCreatedAtDesc(providerId);
    }

    private void requestTransfer(Payment payment, ProviderSettlement settlement) {
        try {
            if (payment.getRazorpayPaymentId() == null || payment.getRazorpayPaymentId().isBlank()) {
                throw new IllegalStateException("Razorpay payment ID is missing");
            }
            ProviderPayoutDetails provider = providerClient.payoutDetailsFor(payment.getProviderId());
            if (!provider.payoutOnboarded() || provider.razorpayAccountId() == null || provider.razorpayAccountId().isBlank()) {
                throw new IllegalStateException("Provider has not completed payout onboarding");
            }
            ProviderSettlement calculated = applyCommission(settlement, provider.commissionPercentage());
            Map<String, Object> transfer = Map.of(
                    "account", provider.razorpayAccountId(),
                    "amount", toPaise(calculated.getNetAmount()),
                    "currency", "INR",
                    "notes", Map.of("paymentId", payment.getPaymentId().toString(), "sourceId", calculated.getSourceId())
            );
            Map<String, Object> response = razorpayClient.post()
                    .uri("/v1/payments/{paymentId}/transfers", payment.getRazorpayPaymentId())
                    .headers(headers -> headers.setBasicAuth(razorpayKeyId, razorpayKeySecret))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("transfers", List.of(transfer)))
                    .retrieve().body(Map.class);
            calculated.setRazorpayTransferId(extractTransferId(response));
            calculated.setStatus(SettlementStatus.INSTANT_SETTLEMENT_REQUESTED);
            settlementRepo.save(calculated);
            payment.setSettlementStatus(SettlementStatus.INSTANT_SETTLEMENT_REQUESTED);
            paymentRepo.save(payment);
        } catch (Exception exception) {
            settlement.setStatus(SettlementStatus.FAILED);
            settlementRepo.save(settlement);
            payment.setSettlementStatus(SettlementStatus.FAILED);
            paymentRepo.save(payment);
        }
    }

    private ProviderSettlement newSettlement(Payment payment, SettlementStatus status) {
        ProviderSettlement settlement = new ProviderSettlement();
        settlement.setPaymentId(payment.getPaymentId());
        settlement.setSourceType(isSubscription(payment) ? "SUBSCRIPTION" : "ORDER");
        settlement.setSourceId(isSubscription(payment) ? payment.getOrderId().substring("subscription:".length()) : payment.getOrderId());
        settlement.setProviderId(payment.getProviderId());
        settlement.setGrossAmount(payment.getAmount());
        settlement.setCommission(BigDecimal.ZERO.setScale(2));
        settlement.setNetAmount(payment.getAmount());
        settlement.setStatus(status);
        return settlement;
    }

    private ProviderSettlement applyCommission(ProviderSettlement settlement, BigDecimal percentage) {
        if (percentage == null || percentage.signum() < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Provider commission percentage is invalid");
        }
        BigDecimal commission = settlement.getGrossAmount().multiply(percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        settlement.setCommission(commission);
        settlement.setNetAmount(settlement.getGrossAmount().subtract(commission));
        return settlement;
    }

    private int toPaise(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    @SuppressWarnings("unchecked")
    private String extractTransferId(Map<String, Object> response) {
        if (response == null) throw new IllegalStateException("Razorpay returned no transfer response");
        Object transfers = response.get("transfers");
        if (transfers instanceof List<?> list && !list.isEmpty() && list.getFirst() instanceof Map<?, ?> first) {
            Object id = first.get("id");
            if (id != null) return id.toString();
        }
        Object id = response.get("id");
        if (id != null) return id.toString();
        throw new IllegalStateException("Razorpay transfer ID is missing");
    }

    private boolean isSubscription(Payment payment) {
        return payment.getOrderId() != null && payment.getOrderId().startsWith("subscription:");
    }
}
