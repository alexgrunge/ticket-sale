ALTER TABLE office_location ADD COLUMN current_payment BIGINT NOT NULL DEFAULT 0;
ALTER TABLE joined_payment ADD COLUMN payment_number VARCHAR(512);

CREATE VIEW tmp_view AS SELECT joined_payment.id, ol.shift_prefix, row_number() over (partition by ol.shift_prefix order by ol.shift_prefix, pay_date) as rn FROM joined_payment INNER JOIN payment_shift ON payment_shift_id = payment_shift.id INNER JOIN sales_terminal st ON st.id = payment_shift.sales_terminal_id INNER JOIN office_location ol ON ol.id = st.office_location_id WHERE payed = true ORDER BY ol.shift_prefix, pay_date ASC;

UPDATE joined_payment SET payment_number = rn FROM tmp_view WHERE joined_payment.id = tmp_view.id;

DROP VIEW tmp_view;

CREATE VIEW tmp_view AS SELECT ol.shift_prefix, count(*) as cnt FROM joined_payment INNER JOIN payment_shift ON payment_shift_id = payment_shift.id INNER JOIN sales_terminal st ON st.id = payment_shift.sales_terminal_id INNER JOIN office_location ol ON ol.id = st.office_location_id WHERE payed = true GROUP BY ol.shift_prefix;

UPDATE office_location SET current_payment = cnt FROM tmp_view WHERE office_location.shift_prefix = tmp_view.shift_prefix;

DROP VIEW tmp_view;

