CREATE TABLE centers
(
    id          UUID         NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP WITHOUT TIME ZONE,
    deleted_by  VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    location    VARCHAR(255),
    category    VARCHAR(255),
    owner_id    UUID         NOT NULL,
    CONSTRAINT pk_centers PRIMARY KEY (id)
);