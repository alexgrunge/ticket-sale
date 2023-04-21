CREATE TABLE package_ticket (
  id VARCHAR(255) NOT NULL,
  sale_id VARCHAR(256) NOT NULL REFERENCES internet_sale(id),
  price NUMERIC(15, 3) NOT NULL,
  concept VARCHAR(512) NOT NULL,
  sender_name VARCHAR(512) NOT NULL,
  receiver_name VARCHAR(512) NOT NULL,
  contact_data VARCHAR(512) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);
