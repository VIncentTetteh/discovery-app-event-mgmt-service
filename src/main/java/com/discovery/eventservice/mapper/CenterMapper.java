package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.CenterRequest;
import com.discovery.eventservice.dto.response.CenterCategoryResponse;
import com.discovery.eventservice.dto.response.CenterResponse;
import com.discovery.eventservice.model.Center;
import com.discovery.eventservice.model.CenterCategory;
import org.mapstruct.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.*;

@Mapper(componentModel = "spring")
public interface CenterMapper {

    Center toEntity(CenterRequest request);

    @Mapping(target = "categories", source = "categories")
    CenterResponse toResponse(Center center);

    List<CenterResponse> toResponseList(List<Center> centers);

    default List<CenterCategoryResponse> mapCategories(Set<CenterCategory> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(cat -> new CenterCategoryResponse(
                        cat.getId(),
                        cat.getName(),
                        cat.getDescription()
                ))
                .toList();
    }
}


