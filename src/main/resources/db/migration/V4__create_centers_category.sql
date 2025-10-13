CREATE TABLE center_category_mappings
(
    category_id UUID NOT NULL,
    center_id   UUID NOT NULL,
    CONSTRAINT pk_center_category_mappings PRIMARY KEY (category_id, center_id)
);

-- CREATE TABLE centers_categories
-- (
--     id          UUID         NOT NULL,
--     name        VARCHAR(255) NOT NULL,
--     description VARCHAR(255),
--     CONSTRAINT pk_centers_categories PRIMARY KEY (id)
-- );

-- ALTER TABLE ticket_types
--     ADD available_quantity INTEGER;

ALTER TABLE ticket_types
    ALTER COLUMN available_quantity SET NOT NULL;

-- ALTER TABLE tickets
--     ADD qr_value VARCHAR(255);

ALTER TABLE tickets
    ALTER COLUMN qr_value SET NOT NULL;

-- ALTER TABLE centers_categories
--     ADD CONSTRAINT uc_centers_categories_name UNIQUE (name);

-- ALTER TABLE tickets
--     ADD CONSTRAINT uc_tickets_qrvalue UNIQUE (qr_value);

ALTER TABLE center_category_mappings
    ADD CONSTRAINT fk_cencatmap_on_center FOREIGN KEY (center_id) REFERENCES centers (id);

ALTER TABLE center_category_mappings
    ADD CONSTRAINT fk_cencatmap_on_center_category FOREIGN KEY (category_id) REFERENCES centers_categories (id);

-- DROP TABLE spatial_ref_sys CASCADE;

-- ALTER TABLE centers
-- DROP
-- COLUMN category;

ALTER TABLE payments
ALTER
COLUMN amount TYPE DECIMAL(10, 2) USING (amount::DECIMAL(10, 2));

ALTER TABLE payments
    ALTER COLUMN amount SET NOT NULL;

ALTER TABLE payments
    ALTER COLUMN event_id SET NOT NULL;

ALTER TABLE payments
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE payments
    ALTER COLUMN user_id SET NOT NULL;

-- CREATE INDEX idx_centers_name ON centers (name);