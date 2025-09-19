package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.response.PaystackInitResponse;
import com.discovery.eventservice.dto.response.PaystackVerifyResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaystackService {
    PaystackInitResponse initializeTransaction(UUID userId, BigDecimal amount, String email, String callbackUrl);
    PaystackVerifyResponse verifyTransaction(String reference);
}

