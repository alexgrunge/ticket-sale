DELETE FROM employee_loan_payment;

ALTER TABLE employee_loan_payment DROP COLUMN date_created;
ALTER TABLE employee_loan_payment DROP COLUMN last_updated;

ALTER TABLE employee_loan_payment ADD COLUMN date_created TIMESTAMP WITH TIME ZONE NOT NULL;
ALTER TABLE employee_loan_payment ADD COLUMN last_updated TIMESTAMP WITH TIME ZONE NOT NULL;

CREATE TABLE sale_shift (
  id character varying(255) NOT NULL PRIMARY KEY,
  user_id VARCHAR(255) NOT NUlL REFERENCES tickets_user(id),
  sales_terminal_id VARCHAR(255) NOT NUlL REFERENCES sales_terminal(id),
  starting_amount NUMERIC(14,4) NOT NULL,
  finished BOOLEAN NOT NULL,
  version BIGINT NOT NULL,
  finish_date timestamp without time zone NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

CREATE TABLE cash_checkpoint (
  id character varying(255) NOT NULL PRIMARY KEY,
  previous_amount NUMERIC(14, 4) NOT NULL,
  new_amount NUMERIC(14, 4) NOT NULL,
  sales_shift_id VARCHAR(255) NOT NULL REFERENCES sale_shift(id),
  version BIGINT NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

CREATE TABLE payment_shift (
  id character varying(255) NOT NULL PRIMARY KEY,
  user_id VARCHAR(255) NOT NUlL REFERENCES tickets_user(id),
  sales_terminal_id VARCHAR(255) NOT NUlL REFERENCES sales_terminal(id),
  starting_amount NUMERIC(14,4) NOT NULL,
  finished BOOLEAN NOT NULL,
  version BIGINT NOT NULL,
  finish_date timestamp without time zone NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

CREATE TABLE payment_cash_checkpoint (
  id character varying(255) NOT NULL PRIMARY KEY,
  previous_amount NUMERIC(14, 4) NOT NULL,
  new_amount NUMERIC(14, 4) NOT NULL,
  payment_shift_id VARCHAR(255) NOT NULL REFERENCES payment_shift(id),
  version BIGINT NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);
