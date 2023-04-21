CREATE TABLE global_config (
  id VARCHAR(255) NOT NULL,
  json_obj TEXT NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY(id)
);
