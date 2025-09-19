package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.PaymentRequest;
import com.discovery.eventservice.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiatePayment(PaymentRequest request);
    PaymentResponse confirmPayment(String reference);
    List<PaymentResponse> getUserPayments(UUID userId);
}

