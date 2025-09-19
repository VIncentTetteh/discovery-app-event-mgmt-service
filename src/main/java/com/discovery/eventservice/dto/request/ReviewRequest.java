package com.discovery.eventservice.dto.request;

import java.util.UUID;

public record ReviewRequest(
        int rating,
        String comment,
        UUID eventId,
        UUID userId
) {}

