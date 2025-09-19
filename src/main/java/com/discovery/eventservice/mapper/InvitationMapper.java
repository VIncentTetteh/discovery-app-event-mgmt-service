package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.InvitationRequest;
import com.discovery.eventservice.dto.response.InvitationResponse;
import com.discovery.eventservice.model.Invitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    Invitation toEntity(InvitationRequest request);

    InvitationResponse toResponse(Invitation invitation);
}

