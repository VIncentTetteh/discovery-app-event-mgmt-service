package com.discovery.eventservice.model;

import com.discovery.eventservice.enums.PaymentStatus;
import com.discovery.eventservice.util.Uuid7Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(generator = "uuid7")
    @GenericGenerator(name = "uuid7", type = Uuid7Type.class)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    private UUID userId;
    private UUID eventId;

    private UUID ticketTypeId; // to know which tickets to issue
    private int quantity;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(unique = true, nullable = false)
    private String reference; // Paystack reference

    private String authorizationUrl; // Paystack checkout link


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

