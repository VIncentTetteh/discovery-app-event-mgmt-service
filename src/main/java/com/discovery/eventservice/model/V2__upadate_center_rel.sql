CREATE TABLE center_category_map
(
    category_id UUID NOT NULL,
    center_id   UUID NOT NULL,
    CONSTRAINT pk_center_category_map PRIMARY KEY (category_id, center_id)
);

CREATE TABLE centers
(
    id          UUID         NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP WITHOUT TIME ZONE,
    deleted_by  VARCHAR(255),
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    location    VARCHAR(255),
    owner_id    UUID         NOT NULL,
    coordinates GEOMETRY(Point, 4326)       NOT NULL,
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    CONSTRAINT pk_centers PRIMARY KEY (id)
);

ALTER TABLE centers
    ADD CONSTRAINT uc_centers_name UNIQUE (name);

CREATE INDEX idx_centers_coordinates ON centers (coordinates);

CREATE INDEX idx_centers_name ON centers (name);

ALTER TABLE center_category_map
    ADD CONSTRAINT fk_cencatmap_on_center FOREIGN KEY (center_id) REFERENCES centers (id);

ALTER TABLE center_category_map
    ADD CONSTRAINT fk_cencatmap_on_center_category FOREIGN KEY (category_id) REFERENCES centers_categories (id);

DROP TABLE spatial_ref_sys CASCADE;