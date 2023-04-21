ALTER TABLE internet_sale ADD COLUMN short_id VARCHAR(512) NOT NULL;
ALTER TABLE internet_sale ADD CONSTRAINT unique_short_id UNIQUE (short_id);
