package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.ReviewRequest;
import com.discovery.eventservice.dto.response.ReviewResponse;
import com.discovery.eventservice.model.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toEntity(ReviewRequest request);

    ReviewResponse toResponse(Review review);
}

