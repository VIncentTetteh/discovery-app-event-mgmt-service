package com.discovery.eventservice.dto.response;

import java.math.BigDecimal;

public record PaystackVerifyResponse(
        String status,
        String reference,
        BigDecimal amount
) {}
