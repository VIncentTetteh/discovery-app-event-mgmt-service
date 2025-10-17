package com.discovery.eventservice.model;

import com.discovery.eventservice.util.Uuid7Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends BaseEntity{
    @Id
    @GeneratedValue(generator = "uuid7")
    @GenericGenerator(name = "uuid7", type = Uuid7Type.class)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    private String paystackSubscriptionCode;
    private String paystackCustomerCode;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}

