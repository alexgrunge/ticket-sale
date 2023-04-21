CREATE OR REPLACE VIEW trip_departure_time AS
SELECT
  id,
  extract(YEAR FROM departure_date AT TIME ZONE 'America/Mexico_City') AS year,
  extract(QUARTER FROM departure_date AT TIME ZONE 'America/Mexico_City') AS quarter,
  extract(MONTH FROM departure_date AT TIME ZONE 'America/Mexico_City') AS month,
  extract(HOUR FROM departure_date AT TIME ZONE 'America/Mexico_City') AS hour,
  extract(WEEK FROM departure_date AT TIME ZONE 'America/Mexico_City') AS week,
  extract(DOW FROM departure_date AT TIME ZONE 'America/Mexico_City') AS day_of_week,
  extract(DOY FROM departure_date AT TIME ZONE 'America/Mexico_City') AS day_of_year
FROM trip;

CREATE OR REPLACE VIEW trip_arrival_time AS
SELECT
  id,
  extract(YEAR FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS year,
  extract(QUARTER FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS quarter,
  extract(MONTH FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS month,
  extract(HOUR FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS hour,
  extract(WEEK FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS week,
  extract(DOW FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS day_of_week,
  extract(DOY FROM estimated_arrival AT TIME ZONE 'America/Mexico_City') AS day_of_year
FROM trip;

CREATE OR REPLACE VIEW ticket_sale_time AS
SELECT
  id,
  extract(YEAR FROM date_created AT TIME ZONE 'America/Mexico_City') AS year,
  extract(QUARTER FROM date_created AT TIME ZONE 'America/Mexico_City') AS quarter,
  extract(MONTH FROM date_created AT TIME ZONE 'America/Mexico_City') AS month,
  extract(HOUR FROM date_created AT TIME ZONE 'America/Mexico_City') AS hour,
  extract(WEEK FROM date_created AT TIME ZONE 'America/Mexico_City') AS week,
  extract(DOW FROM date_created AT TIME ZONE 'America/Mexico_City') AS day_of_week,
  extract(DOY FROM date_created AT TIME ZONE 'America/Mexico_City') AS day_of_year
FROM trip_seat;

CREATE OR REPLACE VIEW trip_details AS
SELECT
  t.id,
  SUM(s.kilometers) AS total_kilometers,
  SUM(s.travel_minutes + s.waiting_minutes) AS total_minutes,
  SUM(s.travel_minutes) AS travel_minutes,
  SUM(s.travel_minutes) AS waiting_minutes,
  t.diesel_liters / SUM(s.kilometers) AS diesel_per_kilometer,
  t.diesel_liters * t.diesel_cost AS total_diesel_cost,
  t.diesel_liters / SUM(s.kilometers) * t.diesel_cost AS kilometer_diesel_cost
FROM trip t
INNER JOIN product p ON t.run_id = p.id
INNER JOIN run_stop_off rso ON rso.run_stops_id = p.id
INNER JOIN stop_off s ON rso.stop_off_id = s.id
WHERE t.payed = TRUE
GROUP BY
  t.id,
  t.diesel_liters,
  t.diesel_cost;

CREATE VIEW trip_data AS
SELECT
  t.*,
  td.total_kilometers,
  td.total_minutes,
  td.travel_minutes,
  td.waiting_minutes,
  td.diesel_per_kilometer,
  td.total_diesel_cost,
  td.kilometer_diesel_cost
FROM trip t
INNER JOIN trip_details td ON t.id = td.id;
