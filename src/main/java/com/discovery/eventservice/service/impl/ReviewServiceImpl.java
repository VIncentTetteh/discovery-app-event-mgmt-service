package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.ReviewRequest;
import com.discovery.eventservice.dto.response.ReviewResponse;
import com.discovery.eventservice.mapper.ReviewMapper;
import com.discovery.eventservice.model.Event;
import com.discovery.eventservice.model.Review;
import com.discovery.eventservice.repository.EventRepository;
import com.discovery.eventservice.repository.ReviewRepository;
import com.discovery.eventservice.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse addReview(ReviewRequest request) {
        // Ensure event exists
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + request.eventId()));

        Review review = reviewMapper.toEntity(request);
        review.setEvent(event);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public List<ReviewResponse> getReviewsByEvent(UUID eventId) {
        return reviewRepository.findByEventId(eventId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<ReviewResponse> getReviewsByUser(UUID userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }
}

