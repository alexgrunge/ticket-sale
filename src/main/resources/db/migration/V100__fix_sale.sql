ALTER TABLE internet_sale ADD COLUMN email VARCHAR(512) NOT NULL;
ALTER TABLE trip_seat ADD CONSTRAINT unique_ticket_id UNIQUE (ticket_id);
