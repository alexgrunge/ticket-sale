CREATE TABLE bus_location (
    id VARCHAR(255) NOT NULL,
    bus_id VARCHAR(255) NOT NULL REFERENCES bus(id),
    location VARCHAR(512),
    heading VARCHAR(512),
    event VARCHAR(512),
    zones VARCHAR(512),
    latitude NUMERIC(24, 20),
    longitude NUMERIC(24, 20),
    gps_time TIMESTAMP WITH TIME ZONE NOT NULL,
    server_time TIMESTAMP WITH TIME ZONE NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL
);
