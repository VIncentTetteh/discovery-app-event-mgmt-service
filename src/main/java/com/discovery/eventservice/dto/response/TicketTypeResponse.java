package com.discovery.eventservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        BigDecimal price,
        int quantity,
        UUID eventId
) {}

