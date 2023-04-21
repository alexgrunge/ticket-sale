ALTER TABLE route_type_price DROP COLUMN service_type_id;

DELETE FROM run_service_type_time;
DELETE FROM service_type_time_days;
DELETE FROM service_type_time;

ALTER TABLE service_type_time ALTER COLUMN service_type_id DROP NOT NULL;
ALTER TABLE service_type_time ADD COLUMN service_level_type_id VARCHAR(255) REFERENCES service_level_type(id) NOT NULL;

UPDATE route SET route_type_id = 'long-id';
ALTER TABLE route ALTER COLUMN route_type_id SET NOT NULL;
