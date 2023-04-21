ALTER TABLE individual_debt ADD COLUMN payment_number INTEGER;
ALTER TABLE individual_debt ADD COLUMN percentage NUMERIC(10,4);

ALTER TABLE individual_debt ADD CONSTRAINT nonnegative_debt CHECK (missing_amount >= 0);
ALTER TABLE individual_debt ADD CONSTRAINT payment_type CHECK (payment_number IS NOT NULL OR percentage IS NOT NULL);
