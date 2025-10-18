CREATE TABLE centers_categories
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
    CONSTRAINT pk_centers_categories PRIMARY KEY (id)
);

ALTER TABLE centers_categories
    ADD CONSTRAINT uc_centers_categories_name UNIQUE (name);

DROP TABLE spatial_ref_sys CASCADE;