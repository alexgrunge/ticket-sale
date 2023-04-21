ALTER TABLE employee_event ADD COLUMN job_detail_id VARCHAR(255) REFERENCES job_detail(id);

ALTER TABLE employee_event ALTER COLUMN employee_position_id DROP NOT NULL;
