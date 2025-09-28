package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.request.InvitationRequest;
import com.discovery.eventservice.dto.response.InvitationResponse;
import com.discovery.eventservice.enums.InvitationStatus;
import com.discovery.eventservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    /**
     * Send an invitation for an event
     */
    @PostMapping("/send")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public InvitationResponse sendInvitation(@RequestBody InvitationRequest request,
                                             @AuthenticationPrincipal String userId,
                                             @AuthenticationPrincipal(expression = "authorities") Collection<?> authorities) throws AccessDeniedException {
        boolean isAdmin = authorities.stream().anyMatch(a -> a.toString().equals("ROLE_ADMIN"));
        return invitationService.sendInvitation(request, UUID.fromString(userId), isAdmin);
    }

    /**
     * Respond to an invitation (ACCEPT or DECLINE)
     */
    @PostMapping("/respond/{invitationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public InvitationResponse respondToInvitation(@PathVariable UUID invitationId,
                                                  @RequestParam InvitationStatus status,
                                                  @AuthenticationPrincipal String userId,
                                                  @AuthenticationPrincipal(expression = "authorities") Collection<?> authorities) throws AccessDeniedException {
        boolean isAdmin = authorities.stream().anyMatch(a -> a.toString().equals("ROLE_ADMIN"));
        return invitationService.respondToInvitation(invitationId, status, UUID.fromString(userId), isAdmin);
    }

    /**
     * Get all invitations for a specific event
     */
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<InvitationResponse> getInvitationsByEvent(@PathVariable UUID eventId,
                                                          @AuthenticationPrincipal String userId,
                                                          @AuthenticationPrincipal(expression = "authorities") Collection<?> authorities) throws AccessDeniedException {
        boolean isAdmin = authorities.stream().anyMatch(a -> a.toString().equals("ROLE_ADMIN"));
        return invitationService.getInvitationsByEvent(eventId, UUID.fromString(userId), isAdmin);
    }
}
