ALTER TABLE cancel_event ADD COLUMN cancel_user_id VARCHAR(255) REFERENCES tickets_user(id);
ALTER TABLE cancel_event ADD COLUMN sale_shift_id VARCHAR(255) REFERENCES sale_shift(id);
