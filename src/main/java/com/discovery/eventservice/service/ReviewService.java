package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.ReviewRequest;
import com.discovery.eventservice.dto.response.ReviewResponse;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequest request);
    List<ReviewResponse> getReviewsByEvent(UUID eventId);
    List<ReviewResponse> getReviewsByUser(UUID userId);
}

