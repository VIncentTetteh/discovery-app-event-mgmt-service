package com.discovery.eventservice.dto.request;
public record CenterRequest(
        String name,
        String description,
        String location,   // Human-readable location (e.g., "Osu, Accra")
        String category,
        Double latitude,   // e.g., 5.5560
        Double longitude   // e.g., -0.1969
) {}