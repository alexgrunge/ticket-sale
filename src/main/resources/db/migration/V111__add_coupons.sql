ALTER TABLE payment_coupon ADD COLUMN name VARCHAR (256);

CREATE TABLE coupon_sale (
  id VARCHAR(255) NOT NULL,
  payment_coupon_id VARCHAR(255) REFERENCES payment_coupon(id),
  sales_terminal_id VARCHAR(255) REFERENCES sales_terminal(id),
  salesman_id VARCHAR(255) REFERENCES tickets_user(id),
  date_created TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE internet_sale ADD COLUMN salesman_id VARCHAR(255) REFERENCES tickets_user(id);
ALTER TABLE payment_part ADD COLUMN coupon_sale_id VARCHAR(255) REFERENCES coupon_sale(id);
