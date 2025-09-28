CREATE TABLE tickets
(
    id             UUID                        NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255),
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_by     VARCHAR(255),
    qr_code_key    VARCHAR(255)                NOT NULL,
    status         VARCHAR(255)                NOT NULL,
    is_used        BOOLEAN                     NOT NULL,
    ticket_type_id UUID                        NOT NULL,
    user_id        UUID                        NOT NULL,
    payment_id     UUID,
    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

ALTER TABLE tickets
    ADD CONSTRAINT uc_tickets_qrcodekey UNIQUE (qr_code_key);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payments (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_TICKET_TYPE FOREIGN KEY (ticket_type_id) REFERENCES ticket_types (id);