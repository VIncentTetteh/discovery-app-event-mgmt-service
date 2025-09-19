package com.discovery.eventservice.dto.response;

import java.util.UUID;

public record CenterResponse(
        UUID id,
        String name,
        String description,
        String location,
        String category,
        UUID ownerId
) {}

