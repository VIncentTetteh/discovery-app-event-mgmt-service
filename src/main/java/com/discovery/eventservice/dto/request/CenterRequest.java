package com.discovery.eventservice.dto.request;

public record CenterRequest(
        String name,
        String description,
        String location,
        String category
) {}
