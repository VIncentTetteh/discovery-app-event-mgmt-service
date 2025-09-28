package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.EventRequest;
import com.discovery.eventservice.dto.response.EventResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface EventService {
    EventResponse createEvent(EventRequest request);
    EventResponse getEvent(UUID id);
    List<EventResponse> getEventsByCenter(UUID centerId);
    List<EventResponse> getAllPublicEvents();
    void deleteEvent(UUID id, UUID centerId) throws AccessDeniedException;
//    List<EventResponse> getAllEventsByOwnerID(UUID ownerId);
}

