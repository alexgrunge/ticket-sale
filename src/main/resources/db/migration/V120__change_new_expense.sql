ALTER TABLE trip_expense DROP COLUMN expense_type_data;
ALTER TABLE trip_new_expense ADD COLUMN expense_type_data VARCHAR(255);
