INSERT INTO role(id, name, description, active, date_created, last_updated, version) VALUES('jefe-taquilla', 'Jefe de taquilla', 'Jefe de taquilla', true, current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('config', 'Configurar taquilla', 'Configurar taquilla', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('reports', 'Reportes rápidos', 'Reportes rápidos', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('payments', 'Liquidar', 'Liquidar', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('loans', 'Administrar prestamos', 'Administrar prestamos', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('weekly-payments', 'Pagos semanales', 'Pagos semanales', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('trip-search', 'Busqueda de viajes', 'Busqueda de viajes', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('record-stop', 'Registrar paradas', 'Registrar paradas', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('booth-admin', 'Jefe de taquillas', 'Jefe de taquillas', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('payment-admin', 'Jefe de liquidaciones', 'Jefe de liquidaciones', current_timestamp, current_timestamp, 0);

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'config');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'reports');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'payments');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'loans');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'weekly-payments');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'trip-search');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'record-stop');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'booth-admin');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'payment-admin');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('jefe-taquilla', 'booth-admin');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('jefe-liquidador', 'payment-admin');

