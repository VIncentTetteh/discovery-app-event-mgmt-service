CREATE TABLE reviews
(
    id         UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_by VARCHAR(255),
    rating     INTEGER                     NOT NULL,
    comment    VARCHAR(255),
    user_id    UUID                        NOT NULL,
    event_id   UUID                        NOT NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (id)
);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);