package com.discovery.eventservice.dto.response;

import com.discovery.eventservice.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        String reference,
        BigDecimal amount,
        PaymentStatus status,
        UUID userId,
        UUID eventId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

