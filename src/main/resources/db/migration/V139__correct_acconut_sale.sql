ALTER TABLE account_sale DROP COLUMN sale_shift_id;
ALTER TABLE account_sale ADD COLUMN sales_shift_id VARCHAR(255) REFERENCES sale_shift(id);
