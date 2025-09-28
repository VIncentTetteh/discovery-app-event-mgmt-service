package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.request.PaymentRequest;
import com.discovery.eventservice.dto.request.PurchaseRequest;
import com.discovery.eventservice.dto.request.TicketPurchaseRequest;
import com.discovery.eventservice.dto.response.PaymentResponse;
import com.discovery.eventservice.dto.response.TicketResponse;
import com.discovery.eventservice.service.PaymentService;
import com.discovery.eventservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final PaymentService paymentService;

    // -------------------- USER TICKET ENDPOINTS --------------------

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<TicketResponse> getMyTickets(@AuthenticationPrincipal String userId) {
        return ticketService.getUserTickets(UUID.fromString(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public TicketResponse getTicket(@PathVariable UUID id,
                                    @AuthenticationPrincipal String userId) throws AccessDeniedException {
        TicketResponse ticket = ticketService.getTicket(id);
        if (!ticket.userId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("You are not allowed to view this ticket");
        }
        return ticket;
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('USER')")
    public TicketResponse validateTicket(@RequestParam String qrCode,
                                         @AuthenticationPrincipal String userId) throws AccessDeniedException {
        TicketResponse ticket = ticketService.validateTicket(qrCode);

        if (!ticket.userId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("You are not allowed to validate this ticket");
        }

        return ticket;
    }

    // -------------------- PURCHASE / PAYMENT FLOW --------------------

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('USER')")
    public PaymentResponse purchaseTickets(@RequestBody PurchaseRequest request,
                                           @AuthenticationPrincipal String userId) {

        // Compute total amount (sum of all ticket types × quantities)
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (TicketPurchaseRequest t : request.tickets()) {
            BigDecimal price = ticketService.getTicketTypePrice(t.ticketTypeId());
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(t.quantity())));
        }

        // Build PaymentRequest for PaymentService
        PaymentRequest paymentRequest = new PaymentRequest(
                totalAmount,
                UUID.fromString(userId),
                request.eventId(),
                request.tickets(),        // multiple ticket types
                /* email */ "user@example.com",      // replace with JWT email if available
                /* callbackUrl */ "https://example.com/payment/callback"
        );

        return paymentService.initiatePayment(paymentRequest);
    }

    @PostMapping("/confirm/{reference}")
    @PreAuthorize("hasRole('USER')")
    public List<TicketResponse> confirmPayment(@PathVariable String reference,
                                               @AuthenticationPrincipal String userId) throws AccessDeniedException {

        PaymentResponse payment = paymentService.confirmPayment(reference);

        // Only allow the ticket owner to fetch issued tickets
        if (!payment.userId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("You are not allowed to access these tickets");
        }

        return ticketService.getUserTickets(payment.userId());
    }
}
