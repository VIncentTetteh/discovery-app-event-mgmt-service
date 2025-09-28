package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.request.TicketTypeRequest;
import com.discovery.eventservice.dto.response.TicketTypeResponse;
import com.discovery.eventservice.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    // -------------------- TICKET TYPE ENDPOINTS --------------------

    /**
     * Create a new ticket type for an event
     */
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public TicketTypeResponse createTicketType(@RequestBody TicketTypeRequest request) {
        return ticketTypeService.createTicketType(request);
    }

    /**
     * Get all ticket types for a specific event
     */
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    public List<TicketTypeResponse> getTicketTypesByEvent(@PathVariable UUID eventId) {
        return ticketTypeService.getTicketTypesByEvent(eventId);
    }

    /**
     * Get a specific ticket type by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    public TicketTypeResponse getTicketType(@PathVariable UUID id) {
        return ticketTypeService.getTicketType(id);
    }

    /**
     * Delete a ticket type (only OWNER or ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public void deleteTicketType(@PathVariable UUID id) {
        ticketTypeService.deleteTicketType(id);
    }
}

