package com.discovery.eventservice.dto.request;

import java.util.UUID;

public record SubscriptionRequest(
        UUID userId,
        String email,
        String fullName,
        String planType  // e.g. "PRO"
) {}
