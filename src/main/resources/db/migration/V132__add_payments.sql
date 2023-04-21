CREATE TABLE employee_loan_payment (
  id character varying(255) NOT NULL,
  account_id VARCHAR(255) NOT NUlL REFERENCES employee_account(id),
  loan_id VARCHAR(255) NOT NUlL REFERENCES employee_loan(id),
  amount_payed NUMERIC(14,4) NOT NULL,
  version bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);
