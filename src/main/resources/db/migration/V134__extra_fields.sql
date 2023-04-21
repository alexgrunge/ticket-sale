ALTER TABLE internet_sale ADD COLUMN cash_checkpoint_id VARCHAR(255) REFERENCES cash_checkpoint(id);
ALTER TABLE internet_sale ADD COLUMN sales_shift_id VARCHAR(255) REFERENCES sale_shift(id);
