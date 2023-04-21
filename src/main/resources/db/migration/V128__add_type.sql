ALTER TABLE employee_periodic_payment ADD COLUMN payment_type VARCHAR(128);
CREATE INDEX ON employee_periodic_payment(payment_type);
