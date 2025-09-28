package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.TicketPurchaseRequest;
import com.discovery.eventservice.dto.response.TicketResponse;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TicketService {
    TicketResponse getTicket(UUID id);
    TicketResponse validateTicket(String qrCode);
    List<TicketResponse> getUserTickets(UUID userId);

    @Transactional
    TicketResponse issueTicket(UUID ticketTypeId, UUID userId, UUID paymentId);
    BigDecimal getTicketTypePrice(UUID ticketTypeId);
    List<TicketResponse> issueTickets(UUID userId, UUID paymentId, List<TicketPurchaseRequest> tickets);

//    @Transactional
//    TicketResponse issueTicket(UUID ticketTypeId, UUID userId);
}

