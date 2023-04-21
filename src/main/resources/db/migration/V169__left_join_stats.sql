CREATE TABLE payment_part_type (
    type VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE INDEX payment_type_idx ON payment_part(payment_type);

INSERT INTO payment_part_type VALUES('CASH');
INSERT INTO payment_part_type VALUES('CREDIT_CARD');
INSERT INTO payment_part_type VALUES('ACCOUNT');
INSERT INTO payment_part_type VALUES('TRANSFER');

CREATE OR REPLACE VIEW sale_payment_pivot
AS
SELECT
  ppt.type AS payment_type,
  i.id AS sale_id
FROM
  payment_part_type ppt,
  internet_sale i;

CREATE OR REPLACE VIEW sale_payment_totals
AS
SELECT
  i.id AS sale_id,
  spp.payment_type,
  COALESCE(SUM(pp.amount - pp.change), 0) AS payment_amount
FROM
  internet_sale i
  INNER JOIN
  sale_payment_pivot spp ON i.id = spp.sale_id
  LEFT OUTER JOIN
  payment_part pp ON spp.payment_type = pp.payment_type AND pp.sale_id = i.id
GROUP BY
  i.id,
  spp.payment_type;


CREATE OR REPLACE VIEW cash_sale_totals
AS
SELECT
  sale_id,
  payment_amount AS cash_amount
FROM
  sale_payment_totals
WHERE
  payment_type = 'CASH';

CREATE OR REPLACE VIEW cc_sale_totals
AS
SELECT
  sale_id,
  payment_amount AS cc_amount
FROM
  sale_payment_totals
WHERE
  payment_type = 'CREDIT_CARD';

CREATE OR REPLACE VIEW account_totals
AS
SELECT
  sale_id,
  payment_amount AS account_amount
FROM
  sale_payment_totals
WHERE
  payment_type = 'ACCOUNT';

CREATE OR REPLACE VIEW transfer_totals
AS
SELECT
  sale_id,
  payment_amount AS transfer_amount
FROM
  sale_payment_totals
WHERE
  payment_type = 'TRANSFER';

