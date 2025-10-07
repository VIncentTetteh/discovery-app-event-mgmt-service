package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.response.SubscriptionResponse;
import com.discovery.eventservice.enums.PlanType;

import java.util.UUID;
import java.util.List;

public interface SubscriptionService {

    SubscriptionResponse subscribe(UUID userId, String email, PlanType planType, String name);
    SubscriptionResponse verifySubscription(String subscriptionCode);
    void deactivateExpiredSubscriptions();
    boolean hasActiveSubscription(UUID userId);
    List<SubscriptionResponse> getUserSubscriptions(UUID userId);
}
