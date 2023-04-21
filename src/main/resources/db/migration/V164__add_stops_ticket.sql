DELETE FROM package_ticket;
ALTER TABLE package_ticket ADD COLUMN starting_stop_id VARCHAR(255) NOT NULL REFERENCES stop_off(id);
ALTER TABLE package_ticket ADD COLUMN ending_stop_id VARCHAR(255) NOT NULL REFERENCES stop_off(id);
