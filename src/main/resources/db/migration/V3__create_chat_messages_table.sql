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

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);