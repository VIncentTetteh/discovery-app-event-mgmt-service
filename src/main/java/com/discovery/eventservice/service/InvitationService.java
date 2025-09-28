package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.InvitationRequest;
import com.discovery.eventservice.dto.response.InvitationResponse;
import com.discovery.eventservice.enums.InvitationStatus;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface InvitationService {
    InvitationResponse sendInvitation(InvitationRequest request, UUID userId, boolean isAdmin) throws AccessDeniedException;
    InvitationResponse respondToInvitation(UUID invitationId, InvitationStatus status, UUID userId, boolean isAdmin) throws AccessDeniedException;
//    List<InvitationResponse> getInvitationsByEvent(UUID eventId);
    List<InvitationResponse> getInvitationsByEvent(UUID eventId, UUID userId, boolean isAdmin) throws AccessDeniedException;
    List<InvitationResponse> getInvitationsByUser(UUID userId);

}

