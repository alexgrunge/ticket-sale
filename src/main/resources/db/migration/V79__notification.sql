CREATE TABLE notification (
  id VARCHAR(255) NOT NULL,
  template TEXT NOT NULL,
  cron_check VARCHAR(256) NOT NULL,
  script_id VARCHAR(256) REFERENCES script(id) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);
