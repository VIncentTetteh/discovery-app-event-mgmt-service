package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.config.PaystackConfig;
import com.discovery.eventservice.dto.response.PaystackCustomerResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionInitResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionVerifyResponse;
import com.discovery.eventservice.service.PaystackSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaystackSubscriptionServiceImpl implements PaystackSubscriptionService {

    private final RestTemplate restTemplate;
    private final PaystackConfig paystackConfig;
    private static final String BASE_URL = "https://api.paystack.co";

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackConfig.getSECRET_KEY());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public PaystackCustomerResponse createCustomer(String email, String fullName) {
        log.info("➡️ Creating Paystack customer [email={}]", email);
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("email", email);
            body.put("first_name", fullName.split(" ")[0]);
            if (fullName.contains(" ")) {
                body.put("last_name", fullName.substring(fullName.indexOf(' ') + 1));
            }

            log.debug("📦 Customer request payload: {}", body);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, buildHeaders());
            ResponseEntity<PaystackCustomerResponse> response = restTemplate.exchange(
                    BASE_URL + "/customer",
                    HttpMethod.POST,
                    request,
                    PaystackCustomerResponse.class
            );

            log.info("Customer created successfully [email={}, customerCode={}]",
                    email,
                    response.getBody().getData().getCustomerCode());

            return response.getBody();
        } catch (Exception ex) {
            log.error("Error creating Paystack customer [email={}]: {}", email, ex.getMessage(), ex);
            throw new RuntimeException("Failed to create Paystack customer");
        }
    }

    @Override
    public PaystackSubscriptionInitResponse createSubscription(String customerCode, String planCode) {
        log.info("➡️ Creating subscription [customerCode={}, planCode={}]", customerCode, planCode);
        try {
            Map<String, Object> body = Map.of("customer", customerCode, "plan", planCode);
            log.debug("📦 Subscription request payload: {}", body);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, buildHeaders());
            ResponseEntity<PaystackSubscriptionInitResponse> response = restTemplate.exchange(
                    BASE_URL + "/subscription",
                    HttpMethod.POST,
                    request,
                    PaystackSubscriptionInitResponse.class
            );

            log.info("✅ Subscription created [subscriptionCode={}, customerCode={}]",
                    response.getBody().getData().getSubscriptionCode(),
                    customerCode);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Error creating Paystack subscription [customerCode={}]: {}",
                    customerCode, ex.getMessage(), ex);
            throw new RuntimeException("Failed to create Paystack subscription");
        }
    }

    @Override
    public PaystackSubscriptionVerifyResponse verifySubscription(String subscriptionCode) {
        log.info("➡️ Verifying subscription [subscriptionCode={}]", subscriptionCode);
        try {
            HttpEntity<Void> request = new HttpEntity<>(buildHeaders());
            ResponseEntity<PaystackSubscriptionVerifyResponse> response = restTemplate.exchange(
                    BASE_URL + "/subscription/" + subscriptionCode,
                    HttpMethod.GET,
                    request,
                    PaystackSubscriptionVerifyResponse.class
            );

            log.info("✅ Subscription verification successful [subscriptionCode={}, status={}]",
                    subscriptionCode,
                    response.getBody().getData().getStatus());

            return response.getBody();
        } catch (Exception ex) {
            log.error("Error verifying Paystack subscription [subscriptionCode={}]: {}",
                    subscriptionCode, ex.getMessage(), ex);
            throw new RuntimeException("Failed to verify Paystack subscription");
        }
    }
}
