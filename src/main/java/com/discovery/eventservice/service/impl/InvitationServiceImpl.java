package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.InvitationRequest;
import com.discovery.eventservice.dto.response.InvitationResponse;
import com.discovery.eventservice.enums.InvitationStatus;
import com.discovery.eventservice.mapper.InvitationMapper;
import com.discovery.eventservice.model.Event;
import com.discovery.eventservice.model.Invitation;
import com.discovery.eventservice.repository.EventRepository;
import com.discovery.eventservice.repository.InvitationRepository;
import com.discovery.eventservice.service.InvitationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final InvitationMapper invitationMapper;

    @Override
    public InvitationResponse sendInvitation(InvitationRequest request) {
        // Ensure event exists
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + request.eventId()));

        Invitation invitation = invitationMapper.toEntity(request);
        invitation.setEvent(event);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setSentAt(LocalDateTime.now());

        Invitation saved = invitationRepository.save(invitation);

        // TODO: trigger email service (via RabbitMQ, Kafka, or REST call) to actually send email

        return invitationMapper.toResponse(saved);
    }

    @Override
    public InvitationResponse respondToInvitation(UUID invitationId, InvitationStatus status) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found with id: " + invitationId));

        invitation.setStatus(status);
        invitation.setRespondedAt(LocalDateTime.now());

        Invitation updated = invitationRepository.save(invitation);
        return invitationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public List<InvitationResponse> getInvitationsByEvent(UUID eventId) {
        return invitationRepository.findByEventId(eventId)
                .stream()
                .map(invitationMapper::toResponse)
                .toList();
    }
}

