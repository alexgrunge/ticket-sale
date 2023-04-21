CREATE TABLE joined_payment (
  id VARCHAR(255) NOT NULL,
  payed BOOLEAN DEFAULT false NOT NULL,
  driver1_id VARCHAR(255) REFERENCES individual(id),
  driver2_id VARCHAR(255) REFERENCES individual(id),
  payment_shift_id VARCHAR(255) REFERENCES payment_shift(id),
  payment_terminal_id VARCHAR(255) REFERENCES sales_terminal(id),
  payment_cash_checkpoint_id VARCHAR(255) REFERENCES payment_cash_checkpoint(id),
  driver1_amount NUMERIC(12, 4),
  driver2_amount NUMERIC(12, 4),
  driver1_expenses NUMERIC(12, 4),
  driver2_expenses NUMERIC(12, 4),
  driver1_expenses_advance NUMERIC(12, 4),
  driver2_expenses_advance NUMERIC(12, 4),
  driver1_earnings NUMERIC(12, 4),
  driver2_earnings NUMERIC(12, 4),
  driver1_loans NUMERIC(12, 4),
  driver2_loans NUMERIC(12, 4),
  driver1_nominal NUMERIC(12, 4),
  driver2_nominal NUMERIC(12, 4),
  driver1_insurance NUMERIC(12, 4),
  driver2_insurance NUMERIC(12, 4),
  driver1_nominal_week VARCHAR(250),
  driver2_nominal_week VARCHAR(250),
  driver1_insurance_week VARCHAR(250),
  driver2_insurance_week VARCHAR(250),
  pay_date TIMESTAMP NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE payment_trip_expense(
  payment_id VARCHAR(255) REFERENCES joined_payment(id) NOT NULL,
  trip_expense_id VARCHAR(255) REFERENCES trip_new_expense(id) NOT NULL,
  PRIMARY KEY(payment_id, trip_expense_id)
);

CREATE TABLE payment_trip_advance(
  payment_id VARCHAR(255) REFERENCES joined_payment(id) NOT NULL,
  trip_advance_id VARCHAR(255) REFERENCES trip_advance(id) NOT NULL,
  PRIMARY KEY(payment_id, trip_advance_id)
);

ALTER TABLE trip ADD COLUMN joined_payment_id VARCHAR(255) REFERENCES joined_payment(id);

INSERT INTO joined_payment
    SELECT id,
           payed,
           driver1_id,
           driver2_id,
           payment_shift_id,
           payment_terminal_id,
           payment_cash_checkpoint_id,
           driver1amount,
           driver2amount,
           driver1_expenses,
           driver2_expenses,
           driver1_expenses_advance,
           driver2_expenses_advance,
           driver1_earnings,
           driver2_earnings,
           driver1_loans,
           driver2_loans,
           driver1_nominal,
           driver2_nominal,
           driver1_insurance,
           driver2_insurance,
           driver1_nominal_week,
           driver2_nominal_week,
           driver1_insurance_week,
           driver2_insurance_week,
           pay_date,
           date_created,
           last_updated,
           version
    FROM trip WHERE payed = true;

INSERT INTO payment_trip_expense SELECT trip_expenses_id, trip_expense_id FROM trip_trip_expense WHERE trip_expenses_id IN (SELECT id FROM joined_payment);
INSERT INTO payment_trip_advance SELECT trip_advances_id, trip_advance_id FROM trip_trip_advance WHERE trip_advances_id IN (SELECT id FROM joined_payment);

ALTER TABLE trip DROP COLUMN payed CASCADE;
ALTER TABLE trip DROP COLUMN payment_shift_id CASCADE;
ALTER TABLE trip DROP COLUMN payment_terminal_id CASCADE;
ALTER TABLE trip DROP COLUMN payment_cash_checkpoint_id CASCADE;
ALTER TABLE trip DROP COLUMN driver1amount CASCADE;
ALTER TABLE trip DROP COLUMN driver2amount CASCADE;
ALTER TABLE trip DROP COLUMN driver1_expenses CASCADE;
ALTER TABLE trip DROP COLUMN driver2_expenses CASCADE;
ALTER TABLE trip DROP COLUMN driver1_expenses_advance CASCADE;
ALTER TABLE trip DROP COLUMN driver2_expenses_advance CASCADE;
ALTER TABLE trip DROP COLUMN driver1_earnings CASCADE;
ALTER TABLE trip DROP COLUMN driver2_earnings CASCADE;
ALTER TABLE trip DROP COLUMN driver1_loans CASCADE;
ALTER TABLE trip DROP COLUMN driver2_loans CASCADE;
ALTER TABLE trip DROP COLUMN driver1_nominal CASCADE;
ALTER TABLE trip DROP COLUMN driver2_nominal CASCADE;
ALTER TABLE trip DROP COLUMN driver1_insurance CASCADE;
ALTER TABLE trip DROP COLUMN driver2_insurance CASCADE;
ALTER TABLE trip DROP COLUMN driver1_nominal_week CASCADE;
ALTER TABLE trip DROP COLUMN driver2_nominal_week CASCADE;
ALTER TABLE trip DROP COLUMN driver1_insurance_week CASCADE;
ALTER TABLE trip DROP COLUMN driver2_insurance_week CASCADE;
ALTER TABLE trip DROP COLUMN pay_date CASCADE;

