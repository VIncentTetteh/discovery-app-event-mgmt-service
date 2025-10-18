package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.EventRequest;
import com.discovery.eventservice.dto.response.EventResponse;
import com.discovery.eventservice.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "center.id", target = "centerId")
    @Mapping(source = "center.name", target = "centerName")
    @Mapping(source = "center.latitude", target = "centerLatitude")
    @Mapping(source = "center.longitude", target = "centerLongitude")
    EventResponse toResponse(Event event);

    Event toEntity(EventRequest request);
}


