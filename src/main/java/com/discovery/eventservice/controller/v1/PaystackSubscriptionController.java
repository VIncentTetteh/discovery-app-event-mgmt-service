package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.response.PaystackCustomerResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionInitResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionVerifyResponse;
import com.discovery.eventservice.service.impl.PaystackSubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paystack/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class PaystackSubscriptionController {

    private final PaystackSubscriptionServiceImpl subscriptionService;

    @PostMapping("/customer")
    public ResponseEntity<PaystackCustomerResponse> createCustomer(
            @RequestParam String email,
            @RequestParam String fullName) {

        log.info("📥 [POST] /customer - Creating Paystack customer for email: {}", email);
        PaystackCustomerResponse response = subscriptionService.createCustomer(email, fullName);
        log.info("✅ [POST] /customer - Customer created successfully: {}", response.getData().getCustomerCode());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PaystackSubscriptionInitResponse> createSubscription(
            @RequestParam String customerCode,
            @RequestParam String planCode) {

        log.info("📥 [POST] /subscription - Creating subscription for customer: {}, plan: {}", customerCode, planCode);
        PaystackSubscriptionInitResponse response = subscriptionService.createSubscription(customerCode, planCode);
        log.info("✅ [POST] /subscription - Subscription created successfully: {}", response.getData().getSubscriptionCode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subscriptionCode}/verify")
    public ResponseEntity<PaystackSubscriptionVerifyResponse> verifySubscription(
            @PathVariable String subscriptionCode) {

        log.info("📥 [GET] /verify - Verifying subscription: {}", subscriptionCode);
        PaystackSubscriptionVerifyResponse response = subscriptionService.verifySubscription(subscriptionCode);
        log.info("✅ [GET] /verify - Subscription verified: {}", subscriptionCode);
        return ResponseEntity.ok(response);
    }
}

