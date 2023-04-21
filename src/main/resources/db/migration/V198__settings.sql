CREATE TABLE parameter (
  name VARCHAR(255) NOT NULL PRIMARY KEY,
  value VARCHAR(1024),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL
);

INSERT INTO parameter VALUES('student-discount', 'enabled', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
