ALTER TABLE employee_loan ADD COLUMN missing_amount NUMERIC (12,4) NOT NULL;
ALTER TABLE employee_loan ADD COLUMN payed BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE employee_discount ADD COLUMN loan_id VARCHAR(255) NOT NULL REFERENCES employee_loan(id);
