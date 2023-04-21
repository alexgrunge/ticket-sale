DELETE FROM sales_terminal;

ALTER TABLE sales_terminal DROP COLUMN stop_off_id;

ALTER TABLE sales_terminal ADD COLUMN stop_off_name VARCHAR(256); 
