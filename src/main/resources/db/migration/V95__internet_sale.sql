CREATE TABLE internet_sale (
id VARCHAR(255) NOT NULL PRIMARY KEY,
payment_provider VARCHAR(255) NOT NULL,
payment_id VARCHAR(255) NOT NULL,
source_meta TEXT NOT NULL,
version bigint NOT NULL,
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
UNIQUE (payment_provider, payment_id)
);

ALTER TABLE trip_seat ALTER COLUMN seat_id DROP NOT NULL;
ALTER TABLE trip_seat ADD COLUMN internet_sale_id VARCHAR(255) REFERENCES internet_sale(id);
ALTER TABLE trip_seat ADD COLUMN trip_id VARCHAR(255) REFERENCES trip(id) NOT NULL;
ALTER TABLE trip_seat ADD COLUMN seat_name VARCHAR(255) NOT NULL;
ALTER TABLE trip_seat ADD CONSTRAINT unique_seat UNIQUE (trip_id, seat_name);
