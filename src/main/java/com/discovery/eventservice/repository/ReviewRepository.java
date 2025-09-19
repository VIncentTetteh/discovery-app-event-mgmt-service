package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByEventId(UUID eventId);
    List<Review> findByUserId(UUID userId);
}

