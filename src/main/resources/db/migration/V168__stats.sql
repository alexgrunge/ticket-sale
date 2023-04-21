CREATE OR REPLACE VIEW cash_sale_totals
AS
SELECT
  i.id AS sale_id,
  SUM(pp.amount - pp.change) AS cash_amount
FROM
  internet_sale i
  INNER JOIN
  payment_part pp on pp.sale_id = i.id
  WHERE pp.payment_type = 'CASH'
  GROUP BY i.id;

CREATE OR REPLACE VIEW cc_sale_totals
AS
SELECT
  i.id AS sale_id,
  SUM(pp.amount - pp.change) AS cc_amount
FROM
  internet_sale i
  INNER JOIN
  payment_part pp on pp.sale_id = i.id
  WHERE pp.payment_type = 'CREDIT_CARD'
  GROUP BY i.id;

CREATE OR REPLACE VIEW account_totals
AS
SELECT
  i.id AS sale_id,
  SUM(pp.amount - pp.change) AS account_amount
FROM
  internet_sale i
  INNER JOIN
  payment_part pp on pp.sale_id = i.id
  WHERE pp.payment_type = 'ACCOUNT'
  GROUP BY i.id;

CREATE OR REPLACE VIEW transfer_totals
AS
SELECT
  i.id AS sale_id,
  SUM(pp.amount - pp.change) AS transfer_amount
FROM
  internet_sale i
  INNER JOIN
  payment_part pp on pp.sale_id = i.id
  WHERE pp.payment_type = 'TRANSFER'
  GROUP BY i.id;

