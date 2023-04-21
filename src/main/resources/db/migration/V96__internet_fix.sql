ALTER TABLE internet_sale ALTER COLUMN payment_provider DROP NOT NULL;
ALTER TABLE internet_sale ALTER COLUMN payment_id DROP NOT NULL;
ALTER TABLE internet_sale ALTER COLUMN source_meta DROP NOT NULL;
