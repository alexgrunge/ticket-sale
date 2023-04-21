ALTER TABLE trip ADD COLUMN pay_date TIMESTAMP WITH TIME ZONE;

UPDATE trip SET pay_date = current_timestamp WHERE payed = true;
