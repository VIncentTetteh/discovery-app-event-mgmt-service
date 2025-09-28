package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.request.PaymentRequest;
import com.discovery.eventservice.dto.response.PaymentResponse;
import com.discovery.eventservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // -------------------- PAYMENT ENDPOINTS --------------------

    /**
     * Initiate a payment for one or multiple ticket types
     */
    @PostMapping("/initiate")
    @PreAuthorize("hasRole('USER')")
    public PaymentResponse initiatePayment(@RequestBody PaymentRequest request,
                                           @AuthenticationPrincipal String userId) {

        // Override userId with authenticated user from JWT
        PaymentRequest adjustedRequest = new PaymentRequest(
                request.amount(),
                UUID.fromString(userId),
                request.eventId(),
                request.tickets(),   // multiple ticket types
                request.email(),
                request.callbackUrl()
        );

        return paymentService.initiatePayment(adjustedRequest);
    }

    /**
     * Confirm a payment after user completes checkout
     */
    @PostMapping("/confirm/{reference}")
    @PreAuthorize("hasRole('USER')")
    public PaymentResponse confirmPayment(@PathVariable String reference,
                                          @AuthenticationPrincipal String userId) throws AccessDeniedException {

        PaymentResponse payment = paymentService.confirmPayment(reference);

        // Validate that authenticated user owns this payment
        if (!payment.userId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("You are not allowed to confirm this payment");
        }

        return payment;
    }

    /**
     * Get all payments made by the authenticated user
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<PaymentResponse> getMyPayments(@AuthenticationPrincipal String userId) {
        return paymentService.getUserPayments(UUID.fromString(userId));
    }
}


