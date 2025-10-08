CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE centers
(
    id          UUID         NOT NULL,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    location    VARCHAR(255),
    category    VARCHAR(100),
    owner_id    UUID         NOT NULL,
    coordinates GEOGRAPHY(Point, 4326)      NOT NULL,
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_centers PRIMARY KEY (id)
);

CREATE TABLE chat_messages
(
    id         UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_by VARCHAR(255),
    sender_id  UUID         NOT NULL,
    content    VARCHAR(255) NOT NULL,
    event_id   UUID         NOT NULL,
    sent_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_chat_messages PRIMARY KEY (id)
);

CREATE TABLE events
(
    id          UUID    NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP WITHOUT TIME ZONE,
    deleted_by  VARCHAR(255),
    title       VARCHAR(255),
    description VARCHAR(255),
    start_time  TIMESTAMP WITHOUT TIME ZONE,
    end_time    TIMESTAMP WITHOUT TIME ZONE,
    is_private  BOOLEAN NOT NULL,
    center_id   UUID    NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id)
);

CREATE TABLE invitations
(
    id           UUID         NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255),
    deleted_at   TIMESTAMP WITHOUT TIME ZONE,
    deleted_by   VARCHAR(255),
    email        VARCHAR(255) NOT NULL,
    status       VARCHAR(255),
    event_id     UUID         NOT NULL,
    sent_at      TIMESTAMP WITHOUT TIME ZONE,
    responded_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_invitations PRIMARY KEY (id)
);

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

CREATE TABLE payments
(
    id                UUID         NOT NULL,
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
    reference         VARCHAR(255) NOT NULL,
    authorization_url VARCHAR(255),
    CONSTRAINT pk_payments PRIMARY KEY (id)
);

CREATE TABLE reviews
(
    id         UUID    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_by VARCHAR(255),
    rating     INTEGER NOT NULL,
    comment    VARCHAR(255),
    user_id    UUID    NOT NULL,
    event_id   UUID    NOT NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (id)
);

CREATE TABLE subscription_plans
(
    id                   UUID           NOT NULL,
    name                 VARCHAR(255),
    description          VARCHAR(255),
    monthly_fee          DECIMAL(10, 2) NOT NULL,
    max_private_events   INTEGER        NOT NULL,
    can_upload360        BOOLEAN        NOT NULL,
    can_promote_events   BOOLEAN        NOT NULL,
    can_access_analytics BOOLEAN        NOT NULL,
    paystack_plan_code   VARCHAR(255),
    CONSTRAINT pk_subscription_plans PRIMARY KEY (id)
);

CREATE TABLE subscriptions
(
    id                         UUID    NOT NULL,
    user_id                    UUID    NOT NULL,
    plan_id                    UUID    NOT NULL,
    paystack_subscription_code VARCHAR(255),
    paystack_customer_code     VARCHAR(255),
    start_date                 date,
    end_date                   date,
    active                     BOOLEAN NOT NULL,
    CONSTRAINT pk_subscriptions PRIMARY KEY (id)
);

CREATE TABLE ticket_types
(
    id                   UUID           NOT NULL,
    created_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by           VARCHAR(255),
    updated_by           VARCHAR(255),
    deleted_at           TIMESTAMP WITHOUT TIME ZONE,
    deleted_by           VARCHAR(255),
    name                 VARCHAR(255),
    base_price           DECIMAL(10, 2) NOT NULL,
    final_price          DECIMAL(10, 2) NOT NULL,
    platform_fee_percent DECIMAL(5, 2)  NOT NULL,
    quantity             INTEGER        NOT NULL,
    event_id             UUID           NOT NULL,
    CONSTRAINT pk_ticket_types PRIMARY KEY (id)
);

CREATE TABLE tickets
(
    id             UUID         NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255),
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_by     VARCHAR(255),
    qr_code_key    VARCHAR(255) NOT NULL,
    status         VARCHAR(255) NOT NULL,
    is_used        BOOLEAN      NOT NULL,
    ticket_type_id UUID         NOT NULL,
    user_id        UUID         NOT NULL,
    payment_id     UUID,
    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

ALTER TABLE centers
    ADD CONSTRAINT uc_centers_name UNIQUE (name);

ALTER TABLE payments
    ADD CONSTRAINT uc_payments_reference UNIQUE (reference);

ALTER TABLE tickets
    ADD CONSTRAINT uc_tickets_qrcodekey UNIQUE (qr_code_key);

CREATE INDEX idx_centers_coordinates ON centers (coordinates);

CREATE INDEX idx_centers_name ON centers (name);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);

ALTER TABLE events
    ADD CONSTRAINT FK_EVENTS_ON_CENTER FOREIGN KEY (center_id) REFERENCES centers (id);

ALTER TABLE invitations
    ADD CONSTRAINT FK_INVITATIONS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);

ALTER TABLE payment_tickets
    ADD CONSTRAINT FK_PAYMENT_TICKETS_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payments (id);

ALTER TABLE payment_tickets
    ADD CONSTRAINT FK_PAYMENT_TICKETS_ON_TICKET_TYPE FOREIGN KEY (ticket_type_id) REFERENCES ticket_types (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);

ALTER TABLE subscriptions
    ADD CONSTRAINT FK_SUBSCRIPTIONS_ON_PLAN FOREIGN KEY (plan_id) REFERENCES subscription_plans (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payments (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_TICKET_TYPE FOREIGN KEY (ticket_type_id) REFERENCES ticket_types (id);

ALTER TABLE ticket_types
    ADD CONSTRAINT FK_TICKET_TYPES_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);