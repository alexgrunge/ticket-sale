CREATE TABLE cancel_reservation (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    status VARCHAR(255),
    internet_sale_id VARCHAR(255) REFERENCES internet_sale(id),
    seat_id VARCHAR(255) REFERENCES bus_position(id),
    starting_stop_id VARCHAR(255) REFERENCES stop_off(id),
    ending_stop_id VARCHAR(255) REFERENCES stop_off(id),
    user_id VARCHAR(255) REFERENCES tickets_user(id),
    passenger_type VARCHAR(255),
    sold_price NUMERIC(12,4),
    payed_price NUMERIC(12,4),
    seat_name VARCHAR(255),
    passenger_name VARCHAR(255),
    comments TEXT,
    trip_id VARCHAR(255) REFERENCES trip(id),
    version BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);
