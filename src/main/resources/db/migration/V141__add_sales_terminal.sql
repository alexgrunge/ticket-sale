ALTER TABLE trip ADD COLUMN payment_terminal_id VARCHAR(255) REFERENCES sales_terminal(id);
