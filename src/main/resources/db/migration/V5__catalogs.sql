INSERT INTO employee_position(id, name, date_created, last_updated, version) VALUES('operator-id', 'Operador', current_timestamp, current_timestamp, 0);
INSERT INTO employee_position(id, name, date_created, last_updated, version) VALUES('assistant-id', 'Asistente', current_timestamp, current_timestamp, 0);
INSERT INTO employee_position(id, name, date_created, last_updated, version) VALUES('administrative-id', 'Administrativo', current_timestamp, current_timestamp, 0);
INSERT INTO employee_position(id, name, date_created, last_updated, version) VALUES('driver-id', 'Chofer', current_timestamp, current_timestamp, 0);

INSERT INTO event_type(id, name, description, date_created, last_updated, version) VALUES ('create-employee-id', 'Crear empleado', 'Cuando se crea a un empleado por primera vez', current_timestamp, current_timestamp, 0);
INSERT INTO event_type(id, name, description, date_created, last_updated, version) VALUES ('dismissal-employee-id', 'Despido', 'Evento de despido de un empleado', current_timestamp, current_timestamp, 0);
INSERT INTO event_type(id, name, description, date_created, last_updated, version) VALUES ('rehire-employee-id', 'Recontratación', 'Se vuelve a contratar a un empleado', current_timestamp, current_timestamp, 0);

INSERT INTO bank(id, name, active, date_created, last_updated, version) VALUES ('bancomer-id', 'Bancomer', true, current_timestamp, current_timestamp, 0);
INSERT INTO bank(id, name, active, date_created, last_updated, version) VALUES ('banamex-id', 'Banamex', true, current_timestamp, current_timestamp, 0);
INSERT INTO bank(id, name, active, date_created, last_updated, version) VALUES ('hsbc-id', 'HSBC', true, current_timestamp, current_timestamp, 0);
INSERT INTO bank(id, name, active, date_created, last_updated, version) VALUES ('banorte-id', 'Banorte', true, current_timestamp, current_timestamp, 0);
INSERT INTO bank(id, name, active, date_created, last_updated, version) VALUES ('ixe-id', 'IXE', true, current_timestamp, current_timestamp, 0);
