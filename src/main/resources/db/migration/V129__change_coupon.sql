ALTER TABLE payment_part DROP COLUMN coupon_sale_id;
ALTER TABLE payment_part DROP COLUMN coupon_id;
DROP TABLE coupon_sale;
DROP TABLE payment_coupon;

CREATE TABLE client_account (
  id VARCHAR(255) NOT NULL,
  amount NUMERIC(10, 4) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE account_sale (
  id VARCHAR(255) NOT NULL,
  client_account_id VARCHAR(255) REFERENCES client_account(id),
  sales_terminal_id VARCHAR(255) REFERENCES sales_terminal(id),
  salesman_id VARCHAR(255) REFERENCES tickets_user(id),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE payment_part ADD COLUMN account_sale_id VARCHAR(255) REFERENCES account_sale(id);
