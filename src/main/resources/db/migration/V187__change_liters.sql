ALTER TABLE trip ALTER COLUMN diesel_liters type NUMERIC(10, 4) USING CAST(diesel_liters AS NUMERIC(10, 4));
