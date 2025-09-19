package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.response.PaystackInitResponse;
import com.discovery.eventservice.dto.response.PaystackVerifyResponse;
import com.discovery.eventservice.service.PaystackService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaystackServiceImpl implements PaystackService {

    @Override
    public PaystackInitResponse initializeTransaction(UUID userId, BigDecimal amount, String email, String callbackUrl) {
        // Stub: return fake reference + auth URL
        return new PaystackInitResponse(
                UUID.randomUUID().toString(),
                "https://paystack.com/pay/fake-session"
        );
    }

    @Override
    public PaystackVerifyResponse verifyTransaction(String reference) {
        // Stub: always success for now
        return new PaystackVerifyResponse("success", reference);
    }
}

