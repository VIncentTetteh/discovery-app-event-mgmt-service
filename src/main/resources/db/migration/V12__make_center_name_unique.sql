ALTER TABLE centers
    ADD CONSTRAINT uc_centers_name UNIQUE (name);

ALTER TABLE centers
ALTER
COLUMN name TYPE VARCHAR(150) USING (name::VARCHAR(150));

ALTER TABLE centers
DROP
COLUMN owner_id;

ALTER TABLE centers
    ADD owner_id UUID NOT NULL;