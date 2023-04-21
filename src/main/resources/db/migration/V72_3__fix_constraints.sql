ALTER TABLE individual_debt DROP CONSTRAINT individual_debt_check;

ALTER TABLE individual_debt ADD CONSTRAINT check_type CHECK (
    (authorized = true AND authorizing_individual_id IS NOT NULL AND debt_type = 'LOAN')
    OR
    (authorized = false AND authorizing_individual_id IS NULL AND debt_type = 'LOAN')
    OR
    (authorized IS NULL AND authorizing_individual_id IS NULL AND debt_type != 'LOAN')
    OR
    (authorized = false AND authorizing_individual_id IS NULL AND debt_type != 'LOAN')
);
