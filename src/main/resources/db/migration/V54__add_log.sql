DELETE FROM sale_log;

ALTER TABLE sale_log ADD COLUMN amount NUMERIC(21,2);
