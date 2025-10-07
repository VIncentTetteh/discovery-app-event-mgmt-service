package com.discovery.eventservice.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PaystackInitRequest(
        UUID userId,
        UUID eventId,
        UUID ticketId,
        List<String> ticketNames,
        int quantity,
        BigDecimal amount,
        String email,
        String callbackUrl
) {}

