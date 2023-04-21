ALTER TABLE sale_shift ADD COLUMN current_sale BIGINT NOT NULL DEFAULT 0;
ALTER TABLE internet_sale ADD COLUMN sale_number VARCHAR(255);

CREATE TABLE cancel_event (
    id VARCHAR(255) PRIMARY KEY,
    seat_id VARCHAR(255) REFERENCES bus_position(id),
    starting_stop_id VARCHAR(255) REFERENCES stop_off(id),
    ending_stop_id VARCHAR(255) REFERENCES stop_off(id),
    passenger_type VARCHAR(255) NOT NULL,
    sold_price NUMERIC(10, 4) NOT NULL,
    seat_name VARCHAR(255),
    internet_sale_id VARCHAR(255) REFERENCES internet_sale(id),
    ticket_id VARCHAR(255),
    passenger_name VARCHAR(255),
    trip_id VARCHAR(255) REFERENCES trip(id),
    account_id VARCHAR(255) REFERENCES client_account(id),
    payment_type VARCHAR(255) NOT NULL,
    original_date TIMESTAMP WITH TIME ZONE,
    date_created TIMESTAMP WITH TIME ZONE,
    last_updated TIMESTAMP WITH TIME ZONE,
    version BIGINT
);
