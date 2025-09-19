package com.discovery.eventservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        int rating,
        String comment,
        UUID eventId,
        UUID userId,
        LocalDateTime createdAt
) {}

