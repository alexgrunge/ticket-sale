ALTER TABLE trip ADD COLUMN driver1_expenses NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_expenses NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver1_expenses_advance NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_expenses_advance NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver1_earnings NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_earnings NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver1_retentions NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_retentions NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver1_nominal NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_nominal NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver1_insurance NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver2_insurance NUMERIC(14,4);
ALTER TABLE trip ADD COLUMN driver1_nominal_week VARCHAR(20);
ALTER TABLE trip ADD COLUMN driver2_nominal_week VARCHAR(20);
