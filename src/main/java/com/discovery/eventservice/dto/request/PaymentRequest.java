package com.discovery.eventservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        BigDecimal amount,
        UUID userId,
        UUID eventId,
        UUID ticketTypeId,
        int quantity,
        String email,
        String callbackUrl
) {}


