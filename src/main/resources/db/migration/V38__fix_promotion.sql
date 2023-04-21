ALTER TABLE promotion ALTER COLUMN variables DROP NOT NULL;

INSERT INTO script_category(id, name, version, date_created, last_updated) VALUES ('soporte', 'Soporte', 0, current_timestamp, current_timestamp);
