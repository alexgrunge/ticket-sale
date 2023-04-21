CREATE TABLE billing_address (
  id VARCHAR(255) NOT NULL,
  postal_code VARCHAR(255),
  address VARCHAR(512),
  external_number VARCHAR(255),
  internal_number VARCHAR(255),
  settlement VARCHAR(255),
  block VARCHAR(255),
  municipality VARCHAR(255),
  state VARCHAR(255),
  country VARCHAR(255),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE billing_data (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(512) NOT NULL,
  rfc VARCHAR(256) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE internet_sale ADD COLUMN billing_address_id VARCHAR(255) REFERENCES billing_address(id);
ALTER TABLE internet_sale ADD COLUMN billing_data_id VARCHAR(255) REFERENCES billing_data(id);
ALTER TABLE internet_sale ADD COLUMN bill BOOLEAN DEFAULT false;
