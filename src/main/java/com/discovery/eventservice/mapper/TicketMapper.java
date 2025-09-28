package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.response.TicketResponse;
import com.discovery.eventservice.model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "qrCodeKey", ignore = true) // set later with presigned URL
    @Mapping(source = "ticketType.id", target = "ticketTypeId")
    @Mapping(source = "ticketType.event.id", target = "eventId")
    TicketResponse toResponse(Ticket ticket);
}

