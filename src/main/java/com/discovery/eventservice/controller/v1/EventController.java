package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.request.EventRequest;
import com.discovery.eventservice.dto.response.EventResponse;
import com.discovery.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // -------------------- PUBLIC / USER ENDPOINTS --------------------

    /**
     * Get all public events
     */
    @GetMapping("/public")
    public List<EventResponse> getAllPublicEvents() {
        return eventService.getAllPublicEvents();
    }

    /**
     * Get events by center
     */
    @GetMapping("/center/{centerId}")
    public List<EventResponse> getEventsByCenter(@PathVariable UUID centerId) {
        return eventService.getEventsByCenter(centerId);
    }

    /**
     * Get a specific event by id
     */
    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable UUID id) {
        return eventService.getEvent(id);
    }

    // -------------------- ADMIN / OWNER ENDPOINTS --------------------

    /**
     * Create a new event (Admin / Center Owner only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse createEvent(@RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }

    /**
     * Delete an event (Admin / Center Owner only)
     */
    @DeleteMapping("/{id}/center/{centerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEvent(@PathVariable UUID id, @PathVariable UUID centerId) throws AccessDeniedException {
        eventService.deleteEvent(id, centerId);
    }

    @GetMapping("/nearby")
    public List<EventResponse> getEventsNearbyCenters(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5000") double radius // meters
    ) {
        return eventService.findEventsNearbyCenters(latitude, longitude, radius);
    }

//    /**
//     * Get all events owned by the authenticated admin
//     */
//    @GetMapping("/owner")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<EventResponse> getMyEvents(@AuthenticationPrincipal String ownerId) {
//        return eventService.getAllEventsByOwnerID(UUID.fromString(ownerId));
//    }
}


