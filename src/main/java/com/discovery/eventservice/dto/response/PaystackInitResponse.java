package com.discovery.eventservice.dto.response;

public record PaystackInitResponse(
        String reference,
        String authorizationUrl
) {}
