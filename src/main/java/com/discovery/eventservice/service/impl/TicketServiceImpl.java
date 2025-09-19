package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.response.TicketResponse;
import com.discovery.eventservice.enums.PaymentStatus;
import com.discovery.eventservice.enums.TicketStatus;
import com.discovery.eventservice.mapper.TicketMapper;
import com.discovery.eventservice.model.Payment;
import com.discovery.eventservice.model.Ticket;
import com.discovery.eventservice.model.TicketType;
import com.discovery.eventservice.repository.PaymentRepository;
import com.discovery.eventservice.repository.TicketRepository;
import com.discovery.eventservice.repository.TicketTypeRepository;
import com.discovery.eventservice.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketTypeRepository ticketTypeRepository;
    private final PaymentRepository paymentRepository;
    private final S3Service s3Service;

    @Override
    public TicketResponse getTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        return enrichWithPresignedUrl(ticket);
    }

    @Override
    public TicketResponse validateTicket(String qrValue) {
        // derive S3 key consistently
        String objectKey = "qrcodes/" + qrValue + ".png";

        Ticket ticket = ticketRepository.findByQrCodeKey(objectKey)
                .orElseThrow(() -> new EntityNotFoundException("Invalid or expired ticket QR code"));

        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalStateException("Ticket has already been used");
        }

        // Mark as used
        ticket.setStatus(TicketStatus.USED);
        Ticket updated = ticketRepository.save(ticket);

        return enrichWithPresignedUrl(updated);
    }

    @Override
    public List<TicketResponse> getUserTickets(UUID userId) {
        return ticketRepository.findByUserId(userId)
                .stream()
                .map(this::enrichWithPresignedUrl)
                .toList();
    }

    @Override
    public TicketResponse issueTicket(UUID ticketTypeId, UUID userId, UUID paymentId) {
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket type not found with id: " + ticketTypeId));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot issue ticket: payment not completed");
        }

        // Generate unique QR value
        String qrValue = UUID.randomUUID().toString();

        // Upload QR image to S3 (private)
        String objectKey = s3Service.uploadQrCode(qrValue);

        Ticket ticket = Ticket.builder()
                .ticketType(ticketType)
                .userId(userId)
                .qrCodeKey(objectKey)
                .status(TicketStatus.VALID)
                .payment(payment) // 🔗 link ticket to payment
                .build();

        Ticket saved = ticketRepository.save(ticket);
        return enrichWithPresignedUrl(saved);
    }

    /**
     * Adds presigned URL to TicketResponse.
     */
    private TicketResponse enrichWithPresignedUrl(Ticket ticket) {
        TicketResponse baseResponse = ticketMapper.toResponse(ticket);

        String presignedUrl = s3Service.generatePresignedUrl(
                ticket.getQrCodeKey(),
                Duration.ofMinutes(5)
        );

        return new TicketResponse(
                baseResponse.id(),
                presignedUrl,
                baseResponse.status(),
                baseResponse.isUsed(),
                baseResponse.ticketTypeId(),
                baseResponse.eventId(),
                baseResponse.userId()
        );
    }
}
