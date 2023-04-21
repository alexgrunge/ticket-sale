CREATE TABLE payment_coupon (
  id VARCHAR(255) NOT NULL,
  used BOOLEAN DEFAULT false,
  used_date TIMESTAMP NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE payment_part (
  id VARCHAR(255) NOT NULL,
  coupon_id VARCHAR(255) REFERENCES payment_coupon(id),
  sale_id VARCHAR(255) REFERENCES internet_sale(id),
  reference VARCHAR(255),
  payment_type VARCHAR(255),
  amount NUMERIC(12,4) NOT NULL,
  change NUMERIC(12, 4),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);
