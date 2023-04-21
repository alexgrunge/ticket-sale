ALTER TABLE employee_loan ADD COLUMN trip_id VARCHAR(255) REFERENCES trip(id);
ALTER TABLE employee_loan ADD COLUMN concept VARCHAR(255);
ALTER TABLE employee_loan ADD COLUMN observations VARCHAR(255);
