package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.PaystackInitRequest;
import com.discovery.eventservice.dto.response.PaystackInitResponse;
import com.discovery.eventservice.dto.response.PaystackVerifyResponse;
import com.discovery.eventservice.service.PaystackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaystackServiceImpl implements PaystackService {

    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaystackInitResponse initializeTransaction(PaystackInitRequest request) {
        String url = baseUrl + "/transaction/initialize";
        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.set("Content-Type", "application/json");

        var entity = new org.springframework.http.HttpEntity<>(request, headers);
        var response = restTemplate.postForEntity(url, entity, PaystackInitResponse.class);
        return response.getBody();
    }

    @Override
    public PaystackVerifyResponse verifyTransaction(String reference) {
        String url = baseUrl + "/transaction/verify/" + reference;
        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);

        var entity = new org.springframework.http.HttpEntity<>(headers);
        var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, PaystackVerifyResponse.class);
        return response.getBody();
    }
}
