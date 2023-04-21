ALTER TABLE trip DROP COLUMN service_type_id;
ALTER TABLE trip ADD COLUMN service_level_type_id VARCHAR(255) NOT NULL;
