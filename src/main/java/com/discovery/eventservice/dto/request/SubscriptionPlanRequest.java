package com.discovery.eventservice.dto.request;

import java.math.BigDecimal;

public record SubscriptionPlanRequest(
        String name,                   // FREE, PRO, BUSINESS, ENTERPRISE
        String description,
        BigDecimal monthlyFee,
        int maxPrivateEvents,
        boolean canUpload360,
        boolean canPromoteEvents,
        boolean canAccessAnalytics
) {}

