ALTER TABLE trip DROP COLUMN sales_terminal_id;
ALTER TABLE trip ADD COLUMN pay_terminal_id VARCHAR(255) REFERENCES sales_terminal(id);

ALTER TABLE sales_terminal ADD COLUMN current_payed_amount NUMERIC(20, 4);
