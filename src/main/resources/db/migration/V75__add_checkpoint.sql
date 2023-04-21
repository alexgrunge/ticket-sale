CREATE TABLE individual_debt_checkpoint (
  id VARCHAR(255) NOT NULL,
  payment_type VARCHAR(100) NOT NULL,
  debt_type VARCHAR(100) NOT NULL,
  original_amount NUMERIC(20, 4) NOT NULL,
  missing_amount NUMERIC(20, 4) NOT NULL,
  individual_id VARCHAR(255) NOT NULL REFERENCES individual(id),
  operator_factor NUMERIC(20, 4) NOT NULL,
  single_operator NUMERIC(20, 4) NOT NULL,
  two_operators NUMERIC(20, 4) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE individual_debt_payment ADD COLUMN individual_debt_checkpoint_id VARCHAR(255) NOT NULL REFERENCES individual_debt_checkpoint(id);
