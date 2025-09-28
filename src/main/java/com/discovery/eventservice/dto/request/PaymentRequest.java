package com.discovery.eventservice.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PaymentRequest(
        BigDecimal amount,
        UUID userId,
        UUID eventId,
        List<TicketPurchaseRequest> tickets, // multiple ticket types
        String email,
        String callbackUrl
) {}



