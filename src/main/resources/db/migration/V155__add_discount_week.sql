DELETE FROM employee_periodic_payment;

ALTER TABLE employee_periodic_payment ADD COLUMN payed_trip_id VARCHAR(255) REFERENCES trip(id);
ALTER TABLE employee_periodic_payment ADD COLUMN discount_week VARCHAR(50);
