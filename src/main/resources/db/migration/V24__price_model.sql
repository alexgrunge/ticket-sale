CREATE TABLE route_type (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  version bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

ALTER TABLE route ADD COLUMN route_type_id VARCHAR(255) REFERENCES route_type(id);

CREATE TABLE service_level_type (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  version bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

ALTER TABLE bus ADD COLUMN service_level_type_id VARCHAR(255) REFERENCES service_level_type(id);

CREATE TABLE route_type_price (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  route_type_id VARCHAR(255) NOT NULL REFERENCES route_type(id),
  service_type_id VARCHAR(255) NOT NULL REFERENCES service_type(id),
  service_level_type_id VARCHAR(255) NOT NULL REFERENCES service_level_type(id),
  fixed_price numeric(19, 2) NOT NULL,
  valid_from TIMESTAMP WITHOUT TIME ZONE,
  valid_to TIMESTAMP WITHOUT TIME ZONE,
  version bigint NOT NULL,
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT route_type_unique UNIQUE (route_type_id, service_type_id, service_level_type_id)
);

