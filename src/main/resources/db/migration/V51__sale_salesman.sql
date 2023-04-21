ALTER TABLE sale_log ADD COLUMN sales_person_id VARCHAR(255) REFERENCES tickets_user(id);

