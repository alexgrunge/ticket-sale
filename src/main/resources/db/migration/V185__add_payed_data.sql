ALTER TABLE employee_loan ADD COLUMN payed_trip_id VARCHAR(255) REFERENCES trip(id);

