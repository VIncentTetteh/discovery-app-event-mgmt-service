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

ALTER TABLE invitations
    ADD CONSTRAINT FK_INVITATIONS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);