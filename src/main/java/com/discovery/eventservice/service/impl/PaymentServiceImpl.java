package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.PaymentRequest;
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

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaystackService paystackService;
    private final TicketService ticketService;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        // Call Paystack (stub for now, real API later)
        PaystackInitResponse initResponse = paystackService.initializeTransaction(
                request.userId(),
                request.amount(),
                request.email(),
                request.callbackUrl()
        );

        Payment payment = Payment.builder()
                .userId(request.userId())
                .eventId(request.eventId())
                .ticketTypeId(request.ticketTypeId())
                .quantity(request.quantity())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .reference(initResponse.reference())
                .authorizationUrl(initResponse.authorizationUrl())
                .build();

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toResponse(saved);
    }

    @Override
    public PaymentResponse confirmPayment(String reference) {
        PaystackVerifyResponse verifyResponse = paystackService.verifyTransaction(reference);

        Payment payment = paymentRepository.findByReference(reference)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with reference: " + reference));

        if (verifyResponse.status().equalsIgnoreCase("success")) {
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            // Issue tickets for user
            for (int i = 0; i < payment.getQuantity(); i++) {
                ticketService.issueTicket(payment.getTicketTypeId(), payment.getUserId(), payment.getId());
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
