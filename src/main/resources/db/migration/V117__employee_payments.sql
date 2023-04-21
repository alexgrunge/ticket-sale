CREATE TABLE employee_periodic_payment (
  id VARCHAR(255) NOT NULL,
  employee_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  payed_amount NUMERIC(12, 4) NOT NULL DEFAULT 0,
  payed_date TIMESTAMP WITH TIME ZONE NOT NULL,
  discounted_date TIMESTAMP WITH TIME ZONE,
  version BIGINT NOT NULL,
  date_created TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);
