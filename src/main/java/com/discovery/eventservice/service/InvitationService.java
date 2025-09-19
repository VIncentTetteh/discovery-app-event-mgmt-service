package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.InvitationRequest;
import com.discovery.eventservice.dto.response.InvitationResponse;
import com.discovery.eventservice.enums.InvitationStatus;

import java.util.List;
import java.util.UUID;

public interface InvitationService {
    InvitationResponse sendInvitation(InvitationRequest request);
    InvitationResponse respondToInvitation(UUID invitationId, InvitationStatus status);
    List<InvitationResponse> getInvitationsByEvent(UUID eventId);
}

