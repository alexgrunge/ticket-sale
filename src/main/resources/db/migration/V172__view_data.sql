CREATE OR REPLACE VIEW sale_data AS
SELECT
  i.id,
  i.date_created,
  i.short_id,
  i.bill,
  ss.id as shift_id,
  ss.shift_number,
  tu.id as salesman_id,
  tu.name as salesman_name,
  tu.last_name as salesman_last_name,
  tu.second_last_name as salesman_second_last_name,
  st.id as sales_terminal_id,
  st.terminal_name as sales_terminal_name,
  ol.id as office_location_id,
  ol.name as office_location_name,
  cst.cash_amount,
  ccst.cc_amount,
  act.account_amount,
  tt.transfer_amount,
  cst.cash_amount + ccst.cc_amount + act.account_amount + tt.transfer_amount AS sale_total
FROM internet_sale i
INNER JOIN sale_shift ss ON i.sales_shift_id = ss.id
INNER JOIN tickets_user tu ON ss.user_id = tu.id
INNER JOIN sales_terminal st ON ss.sales_terminal_id = st.id
INNER JOIN office_location ol ON st.office_location_id = ol.id
INNER JOIN cash_sale_totals cst ON i.id = cst.sale_id
INNER JOIN cc_sale_totals ccst ON i.id = ccst.sale_id
INNER JOIN account_totals act ON i.id = act.sale_id
INNER JOIN transfer_totals tt ON i.id = tt.sale_id;

