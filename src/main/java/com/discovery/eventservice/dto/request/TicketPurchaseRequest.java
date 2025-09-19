package com.discovery.eventservice.dto.request;

import java.util.UUID;

public record TicketPurchaseRequest(
        UUID ticketTypeId,
        int quantity
) {}

