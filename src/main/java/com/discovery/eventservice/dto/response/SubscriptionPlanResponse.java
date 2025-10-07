package com.discovery.eventservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record SubscriptionPlanResponse(
        UUID id,
        String name,
        String description,
        BigDecimal monthlyFee,
        int maxPrivateEvents,
        boolean canUpload360,
        boolean canPromoteEvents,
        boolean canAccessAnalytics,
        String paystackPlanCode
) {}

