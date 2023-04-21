ALTER TABLE sale_shift ADD COLUMN chief_user_id VARCHAR(255) REFERENCES tickets_user(id) NOT NULL;
ALTER TABLE payment_shift ADD COLUMN chief_user_id VARCHAR(255) REFERENCES tickets_user(id) NOT NULL;
