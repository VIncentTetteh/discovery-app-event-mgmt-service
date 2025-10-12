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
    private final PaystackServiceImpl paystackService;
    private final TicketService ticketService;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        if (request == null || request.tickets() == null || request.tickets().isEmpty()) {
            throw new IllegalArgumentException("At least one ticket must be provided.");
        }

        if (request.userId() == null || request.eventId() == null ||
                request.amount() == null || request.email() == null) {
            throw new IllegalArgumentException("Missing required payment request fields.");
        }

        // ✅ Step 1: Validate and calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<String> ticketNames = new ArrayList<>();

        List<PaymentTicket> paymentTickets = new ArrayList<>();

        for (TicketPurchaseRequest t : request.tickets()) {
            TicketType ticketType = ticketTypeRepository.findById(t.ticketTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket type not found: " + t.ticketTypeId()));

            if (ticketType.getAvailableQuantity() < t.quantity()) {
                throw new IllegalArgumentException("Not enough tickets available for type: " + ticketType.getName());
            }

            BigDecimal ticketTotal = ticketType.getFinalPrice().multiply(BigDecimal.valueOf(t.quantity()));
            totalAmount = totalAmount.add(ticketTotal);
            ticketNames.add(ticketType.getName());
        }

        if (totalAmount.compareTo(request.amount()) != 0) {
            log.warn("⚠️ Provided amount ({}) doesn’t match computed total ({}).", request.amount(), totalAmount);
            throw new IllegalArgumentException("Provided amount does not match computed ticket total.");
        }

        // ✅ Step 2: Initialize Paystack transaction
        TicketPurchaseRequest firstTicket = request.tickets().get(0);
        PaystackInitRequest paystackInit = new PaystackInitRequest(
                request.userId(),
                request.eventId(),
                firstTicket.ticketTypeId(),
                ticketNames,
                firstTicket.quantity(),
                request.amount(),
                request.email(),
                request.callbackUrl()
        );

        PaystackInitResponse initResponse;
        try {
            initResponse = paystackService.initializeTransaction(paystackInit);
        } catch (Exception e) {
            log.error("Failed to initialize Paystack transaction", e);
            throw new RuntimeException("Payment initialization failed. Please try again.");
        }

        // ✅ Step 3: Create Payment (PENDING)
        Payment payment = Payment.builder()
                .userId(request.userId())
                .eventId(request.eventId())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .reference(initResponse.reference())
                .authorizationUrl(initResponse.authorizationUrl())
                .build();

        // ✅ Step 4: Save PaymentTickets associated with this payment
        for (TicketPurchaseRequest t : request.tickets()) {
            TicketType ticketType = ticketTypeRepository.findById(t.ticketTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket type not found: " + t.ticketTypeId()));

            PaymentTicket pt = PaymentTicket.builder()
                    .ticketType(ticketType)
                    .quantity(t.quantity())
                    .payment(payment)
                    .build();

            paymentTickets.add(pt);
        }

        payment.setTickets(paymentTickets);

        Payment saved = paymentRepository.save(payment);

        log.info("💳 Payment initiated: user={} event={} amount={} ref={}",
                request.userId(), request.eventId(), request.amount(), saved.getReference());

        return paymentMapper.toResponse(saved);
    }

    @Override
    public PaymentResponse confirmPayment(String reference) {
        PaystackVerifyResponse verifyResponse = paystackService.verifyTransaction(reference);

        Payment payment = paymentRepository.findByReference(reference)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with reference: " + reference));

        log.info("[PaymentConfirm] reference={} status={}", reference, verifyResponse.status());

        if (!"success".equalsIgnoreCase(verifyResponse.status())) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            log.warn("Payment {} verification failed with status: {}", reference, verifyResponse.status());
            return paymentMapper.toResponse(payment);
        }

        // Optional: Verify amount matches expected
        if (verifyResponse.amount() != null &&
                verifyResponse.amount().compareTo(payment.getAmount()) != 0) {
            log.error("💣 Amount mismatch for ref={}, expected={}, got={}",
                    reference, payment.getAmount(), verifyResponse.amount());
            throw new SecurityException("Payment amount mismatch detected.");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        // Issue tickets for purchased ticket types
        for (PaymentTicket pt : payment.getTickets()) {
            ticketService.issueTickets(payment.getUserId(), payment.getId(),
                    List.of(new TicketPurchaseRequest(pt.getTicketType().getId(), pt.getQuantity())));
        }

        log.info("Payment {} verified successfully. Tickets issued.", reference);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getUserPayments(UUID userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}
