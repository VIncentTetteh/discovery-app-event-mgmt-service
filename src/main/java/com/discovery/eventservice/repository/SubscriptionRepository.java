package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    /**
     * Find an active subscription by Paystack subscription code.
     *
     * @param paystackSubscriptionCode the unique Paystack subscription code
     * @return optional subscription
     */
    Optional<Subscription> findByPaystackSubscriptionCode(String paystackSubscriptionCode);

    /**
     * Get all active subscriptions.
     *
     * @return list of active subscriptions
     */
    List<Subscription> findByActiveTrue();

    /**
     * Check if a user has an active subscription.
     *
     * @param userId the user id
     * @return true if an active subscription exists
     */
    boolean existsByUserIdAndActiveTrue(UUID userId);

    /**
     * Get all subscriptions belonging to a user.
     *
     * @param userId the user id
     * @return list of user subscriptions
     */
    List<Subscription> findByUserId(UUID userId);
}
