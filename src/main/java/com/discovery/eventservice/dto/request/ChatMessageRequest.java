package com.discovery.eventservice.dto.request;

import java.util.UUID;

public record ChatMessageRequest(
        UUID senderId,
        String content,
        UUID eventId
) {}

