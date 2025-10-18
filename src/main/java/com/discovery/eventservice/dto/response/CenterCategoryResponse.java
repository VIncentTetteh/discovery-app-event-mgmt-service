package com.discovery.eventservice.dto.response;

import java.util.UUID;

public record CenterCategoryResponse (
    UUID id,
    String name,
    String description
) {}
