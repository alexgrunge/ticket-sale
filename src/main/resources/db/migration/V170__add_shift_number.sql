ALTER TABLE sale_shift ADD COLUMN shift_number VARCHAR(255);
ALTER TABLE office_location ADD COLUMN shift_prefix VARCHAR(30);
ALTER TABLE office_location ADD COLUMN current_shift BIGINT NOT NULL DEFAULT 0;

CREATE UNIQUE INDEX unique_shift_number ON sale_shift(shift_number);
