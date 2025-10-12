package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.response.PaystackCustomerResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionInitResponse;
import com.discovery.eventservice.dto.response.PaystackSubscriptionVerifyResponse;

public interface PaystackSubscriptionService {
    PaystackCustomerResponse createCustomer(String email, String fullName);
    PaystackSubscriptionInitResponse createSubscription(String customerCode, String planCode);
    PaystackSubscriptionVerifyResponse verifySubscription(String subscriptionCode);
}
