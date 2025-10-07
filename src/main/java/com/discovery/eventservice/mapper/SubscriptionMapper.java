package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.response.SubscriptionResponse;
import com.discovery.eventservice.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public static SubscriptionResponse toResponse(Subscription sub) {
        return new SubscriptionResponse(
                sub.getId(),
                sub.getUserId(),
                sub.getPlan().getName().name(),
                sub.getPlan().getMonthlyFee(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.isActive(),
                sub.getPaystackSubscriptionCode(),
                sub.getPaystackCustomerCode()
        );
    }
}

