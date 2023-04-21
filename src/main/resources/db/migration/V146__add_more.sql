ALTER TABLE trip ADD COLUMN payment_shift_id VARCHAR(255) REFERENCES payment_shift(id);
