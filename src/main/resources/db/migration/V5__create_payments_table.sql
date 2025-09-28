CREATE TABLE payments
(
    id                UUID                        NOT NULL,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by        VARCHAR(255),
    updated_by        VARCHAR(255),
    deleted_at        TIMESTAMP WITHOUT TIME ZONE,
    deleted_by        VARCHAR(255),
    user_id           UUID,
    event_id          UUID,
    amount            DECIMAL,
    status            VARCHAR(255),
    reference         VARCHAR(255)                NOT NULL,
    authorization_url VARCHAR(255),
    CONSTRAINT pk_payments PRIMARY KEY (id)
);

ALTER TABLE payments
    ADD CONSTRAINT uc_payments_reference UNIQUE (reference);