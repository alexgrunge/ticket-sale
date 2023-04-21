ALTER TABLE internet_sale ADD COLUMN approver_id VARCHAR(255) REFERENCES individual(id);
