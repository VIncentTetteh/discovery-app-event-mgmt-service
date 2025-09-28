CREATE TABLE payment_tickets
(
    id             UUID    NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255),
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_by     VARCHAR(255),
    payment_id     UUID    NOT NULL,
    ticket_type_id UUID    NOT NULL,
    quantity       INTEGER NOT NULL,
    CONSTRAINT pk_payment_tickets PRIMARY KEY (id)
);

ALTER TABLE payment_tickets
    ADD CONSTRAINT FK_PAYMENT_TICKETS_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payments (id);

ALTER TABLE payment_tickets
    ADD CONSTRAINT FK_PAYMENT_TICKETS_ON_TICKET_TYPE FOREIGN KEY (ticket_type_id) REFERENCES ticket_types (id);