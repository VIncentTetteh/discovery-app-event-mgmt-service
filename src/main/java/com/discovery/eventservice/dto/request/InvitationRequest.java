package com.discovery.eventservice.dto.request;

import java.util.UUID;

public record InvitationRequest(
        String email,
        UUID eventId
) {}

