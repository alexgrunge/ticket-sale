ALTER TABLE trip ADD COLUMN correct_departure_date TIMESTAMP WITH TIME ZONE;
UPDATE trip SET correct_departure_date = departure_date;
ALTER TABLE trip DROP COLUMN departure_date ;
ALTER TABLE trip RENAME COLUMN correct_departure_date TO departure_date;
ALTER TABLE trip ALTER COLUMN departure_date SET NOT NULL;
