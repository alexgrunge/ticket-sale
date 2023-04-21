CREATE TABLE trip_stop_occupation(
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    trip_id VARCHAR(255) NOT NULL REFERENCES trip(id),
    stop_off_id VARCHAR(255) NOT NULL REFERENCES stop_off(id),
    ticket_id VARCHAR(255) NOT NULL REFERENCES sale_item(id),
    bus_position_id VARCHAR(255) NOT NULL REFERENCES bus_position(id),
    occupation_type VARCHAR(50) NOT NULL,
    date_created timestamp with time zone NOT NULL,
    last_updated timestamp with time zone NOT NULL
);
