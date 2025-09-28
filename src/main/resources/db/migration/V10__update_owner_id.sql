ALTER TABLE centers
DROP
COLUMN owner_id;

ALTER TABLE centers
    ADD owner_id VARCHAR(255) NOT NULL;