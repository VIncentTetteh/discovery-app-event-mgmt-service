package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.response.PaystackCustomerResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionInitResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionVerifyResponse;
import com.discovery.eventservice.dto.response.SubscriptionResponse;
import com.discovery.eventservice.enums.PlanType;
import com.discovery.eventservice.mapper.SubscriptionMapper;
import com.discovery.eventservice.model.Subscription;
import com.discovery.eventservice.model.SubscriptionPlan;
import com.discovery.eventservice.repository.SubscriptionPlanRepository;
import com.discovery.eventservice.repository.SubscriptionRepository;
import com.discovery.eventservice.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final PaystackSubscriptionServiceImpl paystackService;

    @Override
    public SubscriptionResponse subscribe(UUID userId, String email, PlanType planType, String name) {
        SubscriptionPlan plan = planRepository.findByName(planType)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found: " + planType));

        // Create Paystack Customer & Subscription
        PaystackCustomerResponse customer = paystackService.createCustomer(email, name);
        PaystackSubscriptionInitResponse subscriptionInit = paystackService.createSubscription(
                customer.getData().getCustomerCode(),
                plan.getPaystackPlanCode()
        );

        Subscription subscription = Subscription.builder()
                .userId(userId)
                .plan(plan)
                .paystackCustomerCode(customer.getData().getCustomerCode())
                .paystackSubscriptionCode(subscriptionInit.getData().getSubscriptionCode())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .active(true)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);
        return SubscriptionMapper.toResponse(saved);
    }

    @Override
    public SubscriptionResponse verifySubscription(String subscriptionCode) {
        // Call Paystack API
        PaystackSubscriptionVerifyResponse verifyResponse = paystackService.verifySubscription(subscriptionCode);

        // Get local subscription record
        Subscription subscription = subscriptionRepository.findByPaystackSubscriptionCode(subscriptionCode)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found: " + subscriptionCode));

        // Update subscription based on Paystack status
        String status = verifyResponse.getData().getStatus();
        boolean isActive = "active".equalsIgnoreCase(status);

        subscription.setActive(isActive);

        // Optional: update end date if Paystack provides next payment date
//        if (verifyResponse.getData().getNext_payment_date() != null) {
//            LocalDate nextPaymentDate = LocalDate.parse(verifyResponse.getData().getNext_payment_date().substring(0, 10));
//            subscription.setEndDate(nextPaymentDate);
//        }

        Subscription updated = subscriptionRepository.save(subscription);
        return SubscriptionMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deactivateExpiredSubscriptions() {
        List<Subscription> activeSubs = subscriptionRepository.findByActiveTrue();
        int deactivatedCount = 0;

        for (Subscription sub : activeSubs) {
            if (sub.getEndDate().isBefore(LocalDate.now())) {
                sub.setActive(false);
                subscriptionRepository.save(sub);
                deactivatedCount++;
            }
        }

        log.info("🧾 Deactivated {} expired subscriptions", deactivatedCount);
    }

    @Override
    public boolean hasActiveSubscription(UUID userId) {
        return subscriptionRepository.existsByUserIdAndActiveTrue(userId);
    }

    @Override
    public List<SubscriptionResponse> getUserSubscriptions(UUID userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        return subscriptions.stream()
                .map(SubscriptionMapper::toResponse)
                .toList();
    }
}


