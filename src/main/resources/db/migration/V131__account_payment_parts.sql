ALTER TABLE payment_part ADD COLUMN account_id VARCHAR(255) REFERENCES client_account(id);

