CREATE TABLE ticket_types
(
    id         UUID    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_by VARCHAR(255),
    name       VARCHAR(255),
    price      DECIMAL,
    quantity   INTEGER NOT NULL,
    event_id   UUID    NOT NULL,
    CONSTRAINT pk_ticket_types PRIMARY KEY (id)
);

ALTER TABLE ticket_types
    ADD CONSTRAINT FK_TICKET_TYPES_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);