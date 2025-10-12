package com.discovery.eventservice.model;

import com.discovery.eventservice.util.Uuid7Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "payment_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE payment_tickets SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class PaymentTicket extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid7")
    @GenericGenerator(name = "uuid7", type = Uuid7Type.class)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(nullable = false)
    private int quantity;
}
