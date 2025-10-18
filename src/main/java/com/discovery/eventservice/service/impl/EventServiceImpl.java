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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final SubscriptionServiceImpl subscriptionService;

    @Override
    public EventResponse createEvent(EventRequest request) throws AccessDeniedException {
        // Ensure center exists
        Center center = centerRepository.findById(request.centerId())
                .orElseThrow(() -> new EntityNotFoundException("Center not found with id: " + request.centerId()));

        // ensure same event name does not exist for center
        boolean exists = eventRepository.findByCenterId(center.getId()).stream()
                .anyMatch(e -> e.getTitle().equalsIgnoreCase(request.title()));
        if (exists) {
            throw new IllegalArgumentException("Event with the same title already exists for this center.");
        }



        Event event = eventMapper.toEntity(request);
        event.setCenter(center);



        if (event.isPrivate() || event.getTicketTypes() != null && (!event.getTicketTypes().isEmpty())) {
            if (!subscriptionService.hasActiveSubscription(center.getOwnerId())) {
                throw new AccessDeniedException("You need an active subscription to create private events.");
            }
        }
        Event saved = eventRepository.save(event);
        Event loaded = eventRepository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found after saving"));
        return eventMapper.toResponse(loaded);

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




    @Override
    public List<EventResponse> findEventsNearbyCenters(double latitude, double longitude, double radiusMeters) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);

        List<Event> events = eventRepository.findEventsByNearbyCenters(point, radiusMeters);

        return events.stream()
                .map(event -> new EventResponse(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.isPrivate(),
                        event.getCenter().getId(),
                        event.getCenter().getName(),
                        event.getCenter().getLatitude(),
                        event.getCenter().getLongitude()
                ))
                .toList();
    }


}

