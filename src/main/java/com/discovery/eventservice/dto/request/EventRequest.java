package com.discovery.eventservice.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventRequest(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean isPrivate,
        UUID centerId
) {}

