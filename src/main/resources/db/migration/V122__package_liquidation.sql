ALTER TABLE package_ticket ADD COLUMN payment_price NUMERIC(15, 3) NOT NULL;

CREATE TABLE trip_stop_control (
  id VARCHAR(255) NOT NULL,
  stop_off_id VARCHAR(255) NOT NULL REFERENCES stop_off(id),
  trip_id VARCHAR(255) NOT NULL REFERENCES trip(id),
  visited BOOLEAN NOT NULL DEFAULT false,
  visited_time TIMESTAMP,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);
