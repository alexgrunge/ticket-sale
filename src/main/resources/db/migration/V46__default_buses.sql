CREATE TABLE default_bus (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    trunk_size INTEGER,
    wifi BOOLEAN,
    electric_plugs BOOLEAN,
    headphones BOOLEAN,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    service_level_id VARCHAR(255) NOT NULL REFERENCES service_level_type(id) 
);

CREATE TABLE default_bus_bus_position (
    default_bus_positions_id VARCHAR(255) REFERENCES default_bus(id),
    default_bus_seats_id VARCHAR(255) REFERENCES default_bus(id),
    bus_position_id VARCHAR(255) REFERENCES bus_position(id)
);

INSERT INTO default_bus(id, wifi, electric_plugs, headphones, version, date_created, last_updated, service_level_id) VALUES ('normal-default-bus', false, false, true, 0, current_timestamp, current_timestamp, 'normal-id');

INSERT INTO default_bus(id, wifi, electric_plugs, headphones, version, date_created, last_updated, service_level_id) VALUES ('premium-default-bus', true, true, true, 0, current_timestamp, current_timestamp, 'premium-id');
