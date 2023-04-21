DELETE FROM employee_card_data WHERE card_data_id IN (SELECT cd.id FROM card_data cd INNER JOIN bank b ON cd.bank_id = b.id WHERE b.name = 'Temp Bancomer');
DELETE FROM card_data WHERE bank_id IN (SELECT id FROM bank WHERE name = 'Temp Bancomer');
DELETE FROM bank WHERE name = 'Temp Bancomer';

INSERT INTO route_type(id, name, version, date_created, last_updated) VALUES('long-id', 'Larga', 0, current_timestamp, current_timestamp);
INSERT INTO route_type(id, name, version, date_created, last_updated) VALUES('short-id', 'Corta', 0, current_timestamp, current_timestamp);

INSERT INTO service_level_type(id, name, version, date_created, last_updated) VALUES('premium-id', 'Premium', 0, current_timestamp, current_timestamp);
INSERT INTO service_level_type(id, name, version, date_created, last_updated) VALUES('normal-id', 'Normal', 0, current_timestamp, current_timestamp);
