package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByUserId(UUID userId);

    Optional<Ticket> findByQrCodeKey(String key);

    boolean existsByUserIdAndPaymentIdAndTicketTypeId(UUID userId, UUID paymentId, UUID ticketTypeId);

    Optional<Ticket> findByQrValue(String qrValue);

}

