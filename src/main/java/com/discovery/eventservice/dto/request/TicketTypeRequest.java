package com.discovery.eventservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeRequest(
        UUID eventId,
        String name,
        BigDecimal basePrice,
        int quantity
) {}

