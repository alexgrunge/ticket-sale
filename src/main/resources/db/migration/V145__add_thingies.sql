ALTER TABLE trip DROP COLUMN driver1_amount;
ALTER TABLE trip DROP COLUMN driver2_amount;
ALTER TABLE trip ADD COLUMN driver1amount NUMERIC(14, 4);
ALTER TABLE trip ADD COLUMN driver2amount NUMERIC(14, 4);
