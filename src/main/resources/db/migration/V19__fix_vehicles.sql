ALTER TABLE bus DROP COLUMN owner_name;
ALTER TABLE bus DROP COLUMN owner_rfc;
ALTER TABLE bus DROP COLUMN vehicle_key;

ALTER TABLE bus ADD COLUMN electric_plugs BOOLEAN;
ALTER TABLE bus ADD COLUMN headphones BOOLEAN;

ALTER TABLE bus ADD COLUMN insurance_policy_number VARCHAR(255);
ALTER TABLE bus ADD COLUMN insurance_expiration_date TIMESTAMP;
ALTER TABLE bus ADD COLUMN last_maintenance_date TIMESTAMP;

