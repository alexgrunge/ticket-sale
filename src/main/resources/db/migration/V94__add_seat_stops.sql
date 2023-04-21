ALTER TABLE trip_seat ADD COLUMN starting_stop_id VARCHAR(255) REFERENCES stop_off(id) NOT NULL;
ALTER TABLE trip_seat ADD COLUMN ending_stop_id VARCHAR(255) REFERENCES stop_off(id) NOT NULL;
