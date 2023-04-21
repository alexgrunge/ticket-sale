ALTER TABLE trip ADD COLUMN driver1_loans NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_loans NUMERIC(14,4);

ALTER TABLE trip ADD COLUMN driver1_insurance_week VARCHAR(20);
ALTER TABLE trip ADD COLUMN driver2_insurance_week VARCHAR(20);
