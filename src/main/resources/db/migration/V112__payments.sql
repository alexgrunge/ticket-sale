CREATE TABLE employee_account (
  id VARCHAR(255) NOT NULL,
  current_balance NUMERIC(12, 4) NOT NULL DEFAULT 0,
  employee_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE employee_discount (
  id VARCHAR(255) NOT NULL,
  discount_amount NUMERIC(12, 4) NOT NULL DEFAULT 0,
  employee_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  trip_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  account_id VARCHAR(255) NOT NULL REFERENCES employee_account(id),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE employee_loan (
  id VARCHAR(255) NOT NULL,
  loan_amount NUMERIC(12, 4) NOT NULL DEFAULT 0,
  payment_type VARCHAR(255) NOT NULL DEFAULT 'NUMBER',
  employee_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  account_id VARCHAR(255) NOT NULL REFERENCES employee_account(id),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);
