DELETE FROM trip_expense;

ALTER TABLE trip_expense DROP COLUMN reason;
ALTER TABLE trip_expense DROP COLUMN description;

ALTER TABLE trip_expense ADD COLUMN description VARCHAR(512) NOT NULL;
ALTER TABLE trip_expense ADD COLUMN hasReceipt BOOLEAN NOT NULL;
