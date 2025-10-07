package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.PaymentRequest;
import com.discovery.eventservice.dto.request.PaystackInitRequest;
import com.discovery.eventservice.dto.request.TicketPurchaseRequest;
import com.discovery.eventservice.dto.response.PaymentResponse;
import com.discovery.eventservice.dto.response.PaystackInitResponse;
import com.discovery.eventservice.dto.response.PaystackVerifyResponse;
import com.discovery.eventservice.enums.PaymentStatus;
import com.discovery.eventservice.mapper.PaymentMapper;
import com.discovery.eventservice.model.Payment;
import com.discovery.eventservice.model.TicketType;
import com.discovery.eventservice.repository.PaymentRepository;
import com.discovery.eventservice.service.PaymentService;
import com.discovery.eventservice.service.PaystackService;
import com.discovery.eventservice.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.discovery.eventservice.model.PaymentTicket;
import com.discovery.eventservice.repository.TicketTypeRepository;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PaymentMapper paymentMapper;
    private final PaystackService paystackService;
    private final TicketService ticketService;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        // Validate tickets and calculate total (optional but recommended)
        List<PaymentTicket> paymentTickets = new ArrayList<>();
        List<String> ticketNames = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (TicketPurchaseRequest t : request.tickets()) {
            TicketType ticketType = ticketTypeRepository.findById(t.ticketTypeId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Ticket type not found: " + t.ticketTypeId()));

            BigDecimal ticketTotal = ticketType.getPrice().multiply(BigDecimal.valueOf(t.quantity()));
            totalAmount = totalAmount.add(ticketTotal);
            ticketNames.add(ticketType.getName());

            PaymentTicket pt = PaymentTicket.builder()
                    .ticketType(ticketType)
                    .quantity(t.quantity())
                    .build();
            paymentTickets.add(pt);
        }

        // Safety check
        if (totalAmount.compareTo(request.amount()) != 0) {
            log.warn("⚠️ Provided amount ({}) doesn’t match computed ticket total ({}).", request.amount(), totalAmount);
        }

        // For now, use the first ticket to populate metadata (could extend to all later)
        TicketPurchaseRequest firstTicket = request.tickets().get(0);

        // Initialize Paystack transaction (with metadata)
        PaystackInitRequest paystackInit = new PaystackInitRequest(
                request.userId(),
                request.eventId(),
                firstTicket.ticketTypeId(),
                ticketNames, // can use specific name if single ticket
                firstTicket.quantity(),
                request.amount(),
                request.email(),
                request.callbackUrl()
        );

        PaystackInitResponse initResponse = paystackService.initializeTransaction(paystackInit);

        // Build Payment entity
        Payment payment = Payment.builder()
                .userId(request.userId())
                .eventId(request.eventId())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .reference(initResponse.reference())
                .authorizationUrl(initResponse.authorizationUrl())
                .tickets(paymentTickets)
                .build();

        // Link PaymentTickets to Payment
        paymentTickets.forEach(pt -> pt.setPayment(payment));

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toResponse(saved);
    }

    @Override
    public PaymentResponse confirmPayment(String reference) {
        PaystackVerifyResponse verifyResponse = paystackService.verifyTransaction(reference);

        Payment payment = paymentRepository.findByReference(reference)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with reference: " + reference));

        if ("success".equalsIgnoreCase(verifyResponse.status())) {
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            // Issue tickets for all purchased ticket types and quantities
            for (PaymentTicket pt : payment.getTickets()) {
                for (int i = 0; i < pt.getQuantity(); i++) {
                    ticketService.issueTicket(pt.getTicketType().getId(), payment.getUserId(), payment.getId());
                }
            }

            log.info("Payment {} verified successfully — tickets issued.", reference);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            log.warn("Payment {} verification failed with status: {}", reference, verifyResponse.status());
        }

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public List<PaymentResponse> getUserPayments(UUID userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}