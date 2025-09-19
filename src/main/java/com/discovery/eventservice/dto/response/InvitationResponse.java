package com.discovery.eventservice.dto.response;

import com.discovery.eventservice.enums.InvitationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record InvitationResponse(
        UUID id,
        String email,
        InvitationStatus status,
        UUID eventId,
        LocalDateTime sentAt,
        LocalDateTime respondedAt
) {}

