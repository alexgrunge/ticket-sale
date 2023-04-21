ALTER TABLE trip_expense DROP COLUMN hasReceipt;
ALTER TABLE trip_expense ADD COLUMN has_receipt BOOLEAN NOT NULL;
