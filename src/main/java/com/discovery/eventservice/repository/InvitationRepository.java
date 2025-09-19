package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    List<Invitation> findByEventId(UUID eventId);
    List<Invitation> findByEmail(String email);
}

