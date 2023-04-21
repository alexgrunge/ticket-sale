ALTER TABLE segment_var ADD COLUMN service_level_type_id VARCHAR(255) REFERENCES service_level_type(id);

UPDATE segment_var SET service_level_type_id = 'premium-id';

ALTER TABLE segment_var ALTER COLUMN service_level_type_id SET NOT NULL;
