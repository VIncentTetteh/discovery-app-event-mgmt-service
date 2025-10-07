package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.PaystackInitRequest;
import com.discovery.eventservice.dto.response.PaystackInitResponse;
import com.discovery.eventservice.dto.response.PaystackVerifyResponse;
import com.discovery.eventservice.service.PaystackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaystackServiceImpl implements PaystackService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url:https://api.paystack.co}")
    private String baseUrl;

    @Override
    public PaystackInitResponse initializeTransaction(PaystackInitRequest request) {
        try {
            String url = baseUrl + "/transaction/initialize";

            long amountInMinorUnit = request.amount().multiply(BigDecimal.valueOf(100)).longValue();

            Map<String, Object> metadata = Map.of(
                    "user_id", request.userId().toString(),
                    "event_id", request.eventId().toString(),
                    "ticket_id", request.ticketId().toString(),
                    "ticket_names", request.ticketNames(),
                    "quantity", request.quantity()
            );

            Map<String, Object> body = new HashMap<>();
            body.put("email", request.email());
            body.put("amount", amountInMinorUnit);
            body.put("callback_url", request.callbackUrl());
            body.put("metadata", metadata);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(secretKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, Map.class
            );

            if (response.getBody() == null || response.getBody().get("data") == null) {
                throw new RuntimeException("Invalid Paystack response");
            }

            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

            return new PaystackInitResponse(
                    (String) data.get("reference"),
                    (String) data.get("authorization_url")
            );

        } catch (Exception e) {
            log.error("Error initializing Paystack transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize Paystack transaction");
        }
    }

    @Override
    public PaystackVerifyResponse verifyTransaction(String reference) {
        try {
            String url = baseUrl + "/transaction/verify/" + reference;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(secretKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class
            );

            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            String status = (String) data.get("status");

            return new PaystackVerifyResponse(status, reference);

        } catch (Exception e) {
            log.error("Error verifying Paystack transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to verify Paystack transaction");
        }
    }
}


