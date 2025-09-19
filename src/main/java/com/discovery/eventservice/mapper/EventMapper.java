package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.EventRequest;
import com.discovery.eventservice.dto.response.EventResponse;
import com.discovery.eventservice.model.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEntity(EventRequest request);

    EventResponse toResponse(Event event);
}

