CREATE TABLE office_location (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  address VARCHAR(512),
  latitude numeric(25,8),
  longitude numeric(25,8),
  version bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

ALTER TABLE individual ADD COLUMN office_location_id VARCHAR(255) REFERENCES office_location(id);
