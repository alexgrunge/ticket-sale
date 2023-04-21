CREATE OR REPLACE VIEW sale_time AS
SELECT
  id,
  extract(YEAR FROM date_created AT TIME ZONE 'America/Mexico_City') AS year,
  extract(QUARTER FROM date_created AT TIME ZONE 'America/Mexico_City') AS quarter,
  extract(MONTH FROM date_created AT TIME ZONE 'America/Mexico_City') AS month,
  extract(HOUR FROM date_created AT TIME ZONE 'America/Mexico_City') AS hour,
  extract(WEEK FROM date_created AT TIME ZONE 'America/Mexico_City') AS week,
  extract(DOW FROM date_created AT TIME ZONE 'America/Mexico_City') AS day_of_week,
  extract(DOY FROM date_created AT TIME ZONE 'America/Mexico_City') AS day_of_year
FROM internet_sale;
