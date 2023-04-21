ALTER TABLE service_type ADD COLUMN name VARCHAR(255) NOT NULL;

ALTER TABLE bus ADD COLUMN bus_number VARCHAR(255) NOT NULL;
ALTER TABLE bus ADD COLUMN brand VARCHAR(255) NOT NULL;
ALTER TABLE bus ADD COLUMN model VARCHAR(255) NOT NULL;
ALTER TABLE bus ADD COLUMN year INTEGER NOT NULL;

ALTER TABLE bus ADD COLUMN serial_number VARCHAR(255) NOT NULL;
ALTER TABLE bus ADD COLUMN tag_number VARCHAR(255) NOT NULL;
ALTER TABLE bus ADD COLUMN gps VARCHAR(255);

ALTER TABLE bus ADD COLUMN owner_name VARCHAR(255);
ALTER TABLE bus ADD COLUMN owner_rfc VARCHAR(255);

ALTER TABLE bus ADD COLUMN card_number VARCHAR(255);
ALTER TABLE bus ADD COLUMN card_issue_date TIMESTAMP;
ALTER TABLE bus ADD COLUMN card_expiration_date TIMESTAMP;
ALTER TABLE bus ADD COLUMN card_validity_years INTEGER;

ALTER TABLE bus ADD COLUMN wifi BOOLEAN;

ALTER TABLE bus ADD COLUMN trunk_size INTEGER;
ALTER TABLE bus ADD COLUMN vehicle_key VARCHAR(255);

ALTER TABLE ONLY service_type
    ADD CONSTRAINT service_type_name UNIQUE (name);

CREATE TABLE bus_position (
    id VARCHAR(255) NOT NULL,
    name varchar(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    row_idx INTEGER NOT NULL,
    column_idx INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO service_type(id, name, version, date_created, last_updated) VALUES('turismo', 'Turismo', 0, current_timestamp, current_timestamp);
INSERT INTO service_type(id, name, version, date_created, last_updated) VALUES('pasaje', 'Pasaje', 0, current_timestamp, current_timestamp);

