package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.TicketTypeRequest;
import com.discovery.eventservice.dto.response.TicketTypeResponse;
import com.discovery.eventservice.mapper.TicketTypeMapper;
import com.discovery.eventservice.model.Event;
import com.discovery.eventservice.model.TicketType;
import com.discovery.eventservice.repository.EventRepository;
import com.discovery.eventservice.repository.TicketTypeRepository;
import com.discovery.eventservice.service.TicketTypeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketTypeServiceImpl implements TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;
    private final TicketTypeMapper ticketTypeMapper;

    @Override
    public TicketTypeResponse createTicketType(TicketTypeRequest request) {
        // Ensure the event exists
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + request.eventId()));

        TicketType ticketType = ticketTypeMapper.toEntity(request);
        ticketType.setEvent(event);

        TicketType saved = ticketTypeRepository.save(ticketType);
        return ticketTypeMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public List<TicketTypeResponse> getTicketTypesByEvent(UUID eventId) {
        return ticketTypeRepository.findByEventId(eventId)
                .stream()
                .map(ticketTypeMapper::toResponse)
                .toList();
    }

    @Override
    public TicketTypeResponse getTicketType(UUID id) {
        TicketType ticketType = ticketTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TicketType not found with id: " + id));
        return ticketTypeMapper.toResponse(ticketType);
    }

    @Override
    public void deleteTicketType(UUID id) {
        ticketTypeRepository.deleteById(id);
    }


}

