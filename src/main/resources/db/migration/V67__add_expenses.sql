ALTER TABLE trip ADD COLUMN guide_generated BOOLEAN NOT NULL DEFAULT false;

CREATE TABLE trip_allowance (
  id VARCHAR(255) NOT NULL,
  trip_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  amount NUMERIC(21,2) NOT NULL,
  description VARCHAR(512),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE trip_expense (
  id VARCHAR(255) NOT NULL,
  trip_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  amount NUMERIC(21,2) NOT NULL,
  description VARCHAR(512),
  reason VARCHAR(256) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);


