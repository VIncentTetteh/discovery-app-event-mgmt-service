package com.discovery.eventservice.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        Long senderId,
        String content,
        UUID eventId,
        LocalDateTime sentAt
) {}

