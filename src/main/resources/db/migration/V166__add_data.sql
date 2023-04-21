DELETE FROM trip_seat;
DELETE FROM package_ticket;

ALTER TABLE trip_seat ADD COLUMN passenger_type VARCHAR(255) NOT NULL;
ALTER TABLE trip_seat ADD COLUMN user_id VARCHAR(255) REFERENCES tickets_user(id);
ALTER TABLE trip_seat ADD COLUMN sold_price NUMERIC(12, 4) NOT NULL;

ALTER TABLE package_ticket ADD COLUMN user_id VARCHAR(255) REFERENCES tickets_user(id);
