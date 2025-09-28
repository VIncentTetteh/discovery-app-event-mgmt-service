ALTER TABLE centers
DROP
COLUMN owner_id;

ALTER TABLE centers
    ADD owner_id UUID NOT NULL;