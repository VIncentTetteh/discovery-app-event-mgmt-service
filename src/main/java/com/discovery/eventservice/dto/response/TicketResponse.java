package com.discovery.eventservice.dto.response;

import com.discovery.eventservice.enums.TicketStatus;

import java.util.UUID;
public record TicketResponse(
        UUID id,
        String qrCodeKey, // presigned URL (computed, not stored)
        TicketStatus status,
        boolean isUsed,
        UUID ticketTypeId,
        UUID eventId,
        UUID userId
) {}

