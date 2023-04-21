ALTER TABLE account_sale ADD COLUMN sale_shift_id VARCHAR(255) REFERENCES sale_shift(id);
ALTER TABLE account_sale ADD COLUMN cash_checkpoint_id VARCHAR(255) REFERENCES cash_checkpoint(id);
