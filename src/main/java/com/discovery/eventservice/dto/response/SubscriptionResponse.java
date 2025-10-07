package com.discovery.eventservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        UUID userId,
        String planName,
        BigDecimal monthlyFee,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        String paystackSubscriptionCode,
        String paystackCustomerCode
) {}

