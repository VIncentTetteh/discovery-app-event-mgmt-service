package com.discovery.eventservice.repository;

import com.discovery.eventservice.enums.PlanType;
import com.discovery.eventservice.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {

    /**
     * Finds a subscription plan by its enum name (e.g. BASIC, PREMIUM).
     *
     * @param planType The type of plan.
     * @return Optional containing the subscription plan if found.
     */
    Optional<SubscriptionPlan> findByName(PlanType planType);

    /**
     * Finds a subscription plan by its associated Paystack plan code.
     *
     * @param paystackPlanCode The plan code from Paystack.
     * @return Optional containing the subscription plan if found.
     */
    Optional<SubscriptionPlan> findByPaystackPlanCode(String paystackPlanCode);
}
