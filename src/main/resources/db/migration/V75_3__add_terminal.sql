ALTER TABLE trip ADD COLUMN pay_user_id VARCHAR(255) REFERENCES tickets_user(id);
ALTER TABLE trip ADD COLUMN sales_terminal_id VARCHAR(255) REFERENCES sales_terminal(id);
