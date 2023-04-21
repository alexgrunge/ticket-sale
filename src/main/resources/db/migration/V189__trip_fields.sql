ALTER TABLE employee_loan_payment ADD COLUMN trip_id VARCHAR(255) REFERENCES trip(id);
