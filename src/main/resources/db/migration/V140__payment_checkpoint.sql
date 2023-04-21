ALTER TABLE trip ADD COLUMN payment_cash_checkpoint_id VARCHAR(255) REFERENCES payment_cash_checkpoint(id);
