package com.homebite.payment_service.Scheduler;

import com.homebite.payment_service.Services.SettlementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SettlementScheduler {
    private final SettlementService settlementService;

    public SettlementScheduler(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void runDailySettlement() {
        settlementService.runEndOfDaySettlement();
    }
}
