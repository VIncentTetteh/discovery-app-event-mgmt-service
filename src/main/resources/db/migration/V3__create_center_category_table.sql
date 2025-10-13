CREATE TABLE centers_categories
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_centers_categories PRIMARY KEY (id)
);

-- ALTER TABLE ticket_types
--     ADD available_quantity INTEGER;

-- ALTER TABLE ticket_types
--     ALTER COLUMN available_quantity SET NOT NULL;

ALTER TABLE centers
    ADD category_id UUID;

-- ALTER TABLE tickets
--     ADD qr_value VARCHAR(255);

-- ALTER TABLE tickets
--     ALTER COLUMN qr_value SET NOT NULL;

ALTER TABLE centers_categories
    ADD CONSTRAINT uc_centers_categories_name UNIQUE (name);

-- ALTER TABLE tickets
--     ADD CONSTRAINT uc_tickets_qrvalue UNIQUE (qr_value);

ALTER TABLE centers
    ADD CONSTRAINT FK_CENTERS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES centers_categories (id);

-- DROP TABLE spatial_ref_sys CASCADE;

ALTER TABLE centers
DROP
COLUMN category;

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