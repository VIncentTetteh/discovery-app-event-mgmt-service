package com.discovery.eventservice.model;

import com.discovery.eventservice.enums.PlanType;
import com.discovery.eventservice.util.Uuid7Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "subscription_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {
    @Id
    @GeneratedValue(generator = "uuid7")
    @GenericGenerator(name = "uuid7", type = Uuid7Type.class)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PlanType name; // FREE, PRO, BUSINESS, ENTERPRISE

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyFee;

    private int maxPrivateEvents;
    private boolean canUpload360;
    private boolean canPromoteEvents;
    private boolean canAccessAnalytics;

    private String paystackPlanCode; // generated from Paystack dashboard
}
