ALTER TABLE trip_seat ADD COLUMN original_price numeric(12, 4);
UPDATE trip_seat SET original_price = sold_price;
ALTER TABLE trip_seat ALTER COLUMN original_price SET NOT NULL;
