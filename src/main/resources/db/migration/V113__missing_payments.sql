ALTER TABLE employee_loan ADD COLUMN missing_payment NUMERIC(12, 4) NOT NULL DEFAULT 0;
ALTER TABLE employee_discount ADD COLUMN employee_loan_id VARCHAR(255) NOT NULL REFERENCES employee_loan(id);
