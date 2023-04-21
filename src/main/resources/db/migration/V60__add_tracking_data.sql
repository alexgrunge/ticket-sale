ALTER TABLE route ADD COLUMN tracking_id VARCHAR(255);

ALTER TABLE trip ADD COLUMN off_route BOOLEAN;
ALTER TABLE trip ADD COLUMN currently_travelling BOOLEAN;
ALTER TABLE trip ADD COLUMN delayed_minutes NUMERIC(10,0);
ALTER TABLE trip ADD COLUMN total_travel_minutes NUMERIC(10,0);

ALTER TABLE bus ADD COLUMN off_route BOOLEAN;
ALTER TABLE bus ADD COLUMN currently_delayed BOOLEAN;
ALTER TABLE bus ADD COLUMN currently_travelling BOOLEAN DEFAULT false NOT NULL;
ALTER TABLE bus ADD COLUMN delayed_minutes NUMERIC(10,0);
ALTER TABLE bus ADD COLUMN current_latitude NUMERIC(25,8);
ALTER TABLE bus ADD COLUMN current_longitude NUMERIC(25,8);

