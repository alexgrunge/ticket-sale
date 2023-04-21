CREATE TABLE trip_new_expense (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  expense_type VARCHAR(255),
  amount NUMERIC(12, 4) NOT NULL,
  comments VARCHAR(1024),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL
);

CREATE TABLE trip_advance (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  amount NUMERIC(12, 4) NOT NULL,
  comments VARCHAR(1024),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL
);

CREATE TABLE trip_trip_expense (
  trip_expenses_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  trip_expense_id VARCHAR(255) NOT NULL REFERENCES trip_new_expense(id),
  PRIMARY KEY (trip_expenses_id, trip_expense_id)
);

CREATE TABLE trip_trip_advance (
  trip_advances_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  trip_advance_id VARCHAR(255) NOT NULL REFERENCES trip_advance(id),
  PRIMARY KEY (trip_advances_id, trip_advance_id)
);
