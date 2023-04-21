
ALTER TABLE internet_sale DROP COLUMN approver_id;
ALTER TABLE internet_sale ADD COLUMN approver_id VARCHAR(255) REFERENCES tickets_user(id);
