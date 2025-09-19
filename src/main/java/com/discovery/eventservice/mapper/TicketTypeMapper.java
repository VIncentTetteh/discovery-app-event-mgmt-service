package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.TicketTypeRequest;
import com.discovery.eventservice.dto.response.TicketTypeResponse;
import com.discovery.eventservice.model.TicketType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

    TicketType toEntity(TicketTypeRequest request);

    TicketTypeResponse toResponse(TicketType ticketType);
}

