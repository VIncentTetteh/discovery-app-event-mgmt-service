package com.discovery.eventservice.dto.request;

import java.util.List;
import java.util.UUID;

public record CenterRequest(
        String name,
        String description,
        String location,              // e.g. "Osu, Accra"
        List<UUID> categoryIds,       // Multiple category IDs
        Double latitude,              // e.g. 5.5560
        Double longitude              // e.g. -0.1969
) {}