package com.discovery.eventservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean isPrivate,
        UUID centerId
) {}

