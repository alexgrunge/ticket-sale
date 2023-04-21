CREATE TABLE individual_debt (
  id VARCHAR(255) NOT NULL,
  payment_type VARCHAR(100) NOT NULL,
  debt_type VARCHAR(100) NOT NULL,
  original_amount NUMERIC(20, 4) NOT NULL,
  missing_amount NUMERIC(20, 4) NOT NULL,
  individual_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  authorized BOOLEAN,
  authorizing_individual_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id),
  CHECK (
    (authorized = true AND authorizing_individual_id IS NOT NULL AND debt_type = 'LOAN')
    AND
    (authorized IS NULL AND authorizing_individual_id IS NULL AND debt_type != 'LOAN')
  )
);

CREATE TABLE individual_debt_payment (
  id VARCHAR(255) NOT NULL,
  description VARCHAR(256) NOT NULL,
  debt_id VARCHAR(255) NOT NULL REFERENCES individual_debt(id),
  trip_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  amount NUMERIC(20, 4) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE trip ADD COLUMN payed VARCHAR(255);
