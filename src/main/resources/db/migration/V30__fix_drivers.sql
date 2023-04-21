ALTER TABLE bus ADD COLUMN driver1_id VARCHAR(255) REFERENCES individual(id);
ALTER TABLE bus ADD COLUMN driver2_id VARCHAR(255) REFERENCES individual(id);

ALTER TABLE trip RENAME COLUMN driver_id TO driver1_id;
ALTER TABLE trip ADD COLUMN driver2_id VARCHAR(255) REFERENCES individual(id);
