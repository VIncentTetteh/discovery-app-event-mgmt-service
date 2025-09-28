package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.PaymentRequest;
import com.discovery.eventservice.dto.request.TicketPurchaseRequest;
import com.discovery.eventservice.dto.response.PaymentResponse;
import com.discovery.eventservice.dto.response.PaystackInitResponse;
import com.discovery.eventservice.dto.response.PaystackVerifyResponse;
import com.discovery.eventservice.enums.PaymentStatus;
import com.discovery.eventservice.mapper.PaymentMapper;
import com.discovery.eventservice.model.Payment;
import com.discovery.eventservice.repository.PaymentRepository;
import com.discovery.eventservice.service.PaymentService;
import com.discovery.eventservice.service.PaystackService;
import com.discovery.eventservice.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import com.discovery.eventservice.model.PaymentTicket;
import com.discovery.eventservice.repository.TicketTypeRepository;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PaymentMapper paymentMapper;
    private final PaystackService paystackService;
    private final TicketService ticketService;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        // Initialize Paystack transaction
        PaystackInitResponse initResponse = paystackService.initializeTransaction(
                request.userId(),
                request.amount(),
                request.email(),
                request.callbackUrl()
        );

        // Convert TicketPurchaseRequest to PaymentTicket entities
        List<PaymentTicket> paymentTickets = new ArrayList<>();
        for (TicketPurchaseRequest t : request.tickets()) {
            PaymentTicket pt = PaymentTicket.builder()
                    .ticketType(ticketTypeRepository.findById(t.ticketTypeId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Ticket type not found: " + t.ticketTypeId())))
                    .quantity(t.quantity())
                    .build();
            paymentTickets.add(pt);
        }

        Payment payment = Payment.builder()
                .userId(request.userId())
                .eventId(request.eventId())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .reference(initResponse.reference())
                .authorizationUrl(initResponse.authorizationUrl())
                .tickets(paymentTickets) // associate multiple ticket types
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

            // Issue tickets for all ticket types and quantities
            for (PaymentTicket pt : payment.getTickets()) {
                for (int i = 0; i < pt.getQuantity(); i++) {
                    ticketService.issueTicket(pt.getTicketType().getId(), payment.getUserId(), payment.getId());
                }
            }
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
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
