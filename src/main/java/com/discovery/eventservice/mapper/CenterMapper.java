package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.CenterRequest;
import com.discovery.eventservice.dto.response.CenterResponse;
import com.discovery.eventservice.model.Center;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CenterMapper {

    Center toEntity(CenterRequest request);

    CenterResponse toResponse(Center center);
}

