package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.TicketTypeRequest;
import com.discovery.eventservice.dto.response.TicketTypeResponse;

import java.util.List;
import java.util.UUID;

public interface TicketTypeService {
    TicketTypeResponse createTicketType(TicketTypeRequest request);
    List<TicketTypeResponse> getTicketTypesByEvent(UUID eventId);
    TicketTypeResponse getTicketType(UUID id);
    void deleteTicketType(UUID id);
}

