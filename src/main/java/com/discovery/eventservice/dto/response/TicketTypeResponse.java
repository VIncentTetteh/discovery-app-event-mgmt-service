package com.discovery.eventservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        BigDecimal basePrice,
        BigDecimal finalPrice,
        BigDecimal platformFeePercent,
        int quantity
) {}

