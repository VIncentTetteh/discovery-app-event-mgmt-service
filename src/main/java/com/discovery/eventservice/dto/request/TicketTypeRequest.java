package com.discovery.eventservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeRequest(
        String name,
        BigDecimal price,
        int quantity,
        UUID eventId
) {}

