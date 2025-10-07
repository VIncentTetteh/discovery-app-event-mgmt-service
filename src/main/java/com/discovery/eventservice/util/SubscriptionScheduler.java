package com.discovery.eventservice.util;

import com.discovery.eventservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

    /**
     * Run every day at midnight (00:00) UTC
     * Cron format: second, minute, hour, day, month, day-of-week
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void deactivateExpiredSubscriptionsJob() {
        log.info("⏰ Running scheduled job: deactivateExpiredSubscriptionsJob()");

        try {
            subscriptionService.deactivateExpiredSubscriptions();
            log.info("Completed: Expired subscriptions deactivated successfully.");
        } catch (Exception e) {
            log.error("Failed to deactivate expired subscriptions", e);
        }
    }
}
