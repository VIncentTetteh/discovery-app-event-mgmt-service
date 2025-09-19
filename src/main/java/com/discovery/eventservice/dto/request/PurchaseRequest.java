package com.discovery.eventservice.dto.request;

import java.util.List;
import java.util.UUID;

public record PurchaseRequest(
        UUID eventId,
        UUID userId,
        List<TicketPurchaseRequest> tickets,
        String paymentMethod
) {}

