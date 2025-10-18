package com.discovery.eventservice.dto.response;

import com.discovery.eventservice.model.CenterCategory;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CenterResponse(
        UUID id,
        String name,
        String description,
        String location,
        List<CenterCategoryResponse> categories,
        UUID ownerId,
        Double latitude,
        Double longitude,
        Double distanceMeters
) {}

