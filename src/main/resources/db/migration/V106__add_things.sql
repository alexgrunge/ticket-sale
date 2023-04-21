ALTER TABLE internet_sale ADD COLUMN change_amount NUMERIC(15, 3);
ALTER TABLE internet_sale ADD COLUMN payed_amount NUMERIC(15, 3);
ALTER TABLE internet_sale ADD COLUMN payment_type VARCHAR(255);
ALTER TABLE internet_sale ADD COLUMN payment_origin VARCHAR(255) DEFAULT 'internet';
