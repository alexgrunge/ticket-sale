CREATE TABLE bus_status (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  bus_id VARCHAR(255) NOT NULL REFERENCES bus(id),
  service_type_id VARCHAR(255) NOT NULL REFERENCES service_type(id),
  active BOOLEAN NOT NULL,
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX single_active_bus_status 
  ON bus_status (active)
  WHERE active;

