package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.SubscriptionPlanRequest;
import com.discovery.eventservice.dto.response.SubscriptionPlanResponse;
import com.discovery.eventservice.enums.PlanType;
import com.discovery.eventservice.model.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlan toEntity(SubscriptionPlanRequest request) {
        return SubscriptionPlan.builder()
                .name(PlanType.valueOf(request.name().toUpperCase()))
                .description(request.description())
                .monthlyFee(request.monthlyFee())
                .maxPrivateEvents(request.maxPrivateEvents())
                .canUpload360(request.canUpload360())
                .canPromoteEvents(request.canPromoteEvents())
                .canAccessAnalytics(request.canAccessAnalytics())
                .build();
    }

    public SubscriptionPlanResponse toResponse(SubscriptionPlan plan) {
        return new SubscriptionPlanResponse(
                plan.getId(),
                plan.getName().name(),
                plan.getDescription(),
                plan.getMonthlyFee(),
                plan.getMaxPrivateEvents(),
                plan.isCanUpload360(),
                plan.isCanPromoteEvents(),
                plan.isCanAccessAnalytics(),
                plan.getPaystackPlanCode()
        );
    }
}

