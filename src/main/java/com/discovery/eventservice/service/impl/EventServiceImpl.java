package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.EventRequest;
import com.discovery.eventservice.dto.response.EventResponse;
import com.discovery.eventservice.mapper.EventMapper;
import com.discovery.eventservice.model.Center;
import com.discovery.eventservice.model.Event;
import com.discovery.eventservice.repository.CenterRepository;
import com.discovery.eventservice.repository.EventRepository;
import com.discovery.eventservice.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CenterRepository centerRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse createEvent(EventRequest request) {
        // Ensure center exists
        Center center = centerRepository.findById(request.centerId())
                .orElseThrow(() -> new EntityNotFoundException("Center not found with id: " + request.centerId()));

        Event event = eventMapper.toEntity(request);
        event.setCenter(center);

        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public EventResponse getEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional
    public List<EventResponse> getEventsByCenter(UUID centerId) {
        return eventRepository.findByCenterId(centerId)
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<EventResponse> getAllPublicEvents() {
        return eventRepository.findByIsPrivateFalse()
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteEvent(UUID id, UUID centerId) throws AccessDeniedException {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        if (!event.getCenter().getId().equals(centerId)) {
            throw new AccessDeniedException("You are not allowed to delete this event.");
        }

        eventRepository.delete(event);
    }


}

