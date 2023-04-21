ALTER TABLE cancel_event ADD COLUMN cash_checkpoint_id VARCHAR(255) REFERENCES cash_checkpoint(id);
