create or replace view payment_providers as select provider as id, provider as name from (select distinct payment_provider as provider from internet_sale) s;
create or replace view payment_types as select t as id, t as name from (select distinct payment_type as t from internet_sale) s;

create table billed_catalog (
  id BOOLEAN PRIMARY KEY,
  name VARCHAR(255)
);

INSERT INTO billed_catalog VALUES (true, 'Facturado');
INSERT INTO billed_catalog VALUES (false, 'No Facturado');

create table payed_catalog (
  id BOOLEAN PRIMARY KEY,
  name VARCHAR(255)
);

INSERT INTO payed_catalog VALUES (true, 'Pagado');
INSERT INTO payed_catalog VALUES (false, 'No pagado');

ALTER TABLE internet_sale ADD COLUMN sales_terminal_id VARCHAR(255) REFERENCES sales_terminal(id);
