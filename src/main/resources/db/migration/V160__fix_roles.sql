DELETE FROM tickets_user_role;
DELETE FROM role_permission;
DELETE FROM role;
DELETE FROM permission;

INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-vehicle-id', 'Administrar vehículos', 'Administrar vehículos', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-default-vehicle-id', 'Administrar vehículos default', 'Administrar vehículos default', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-route-id', 'Administrar rutas', 'Administrar rutas', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('block-id', 'Bloqueo de asientos', 'Bloqueo de asientos', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-prices-id', 'Administrar precios', '', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('occupation-id', 'Ocupación', 'Ocupación', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('join-trip-id', 'Unir viajes', 'Unir viajes', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('schedule-route-id', 'Calendarización de rutas', 'Calendarización de rutas', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('assign-buses-id', 'Asignación de autobuses', 'Asignación de autobuses', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('assign-operators-id', 'Asignación de operadores', 'Asignación de operadores', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-users-id', 'Administrar usuarios', 'Administrar usuarios', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-employee-id', 'Administrar empleados', 'Administrar empleados', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('sale-id', 'Venta', 'Venta', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('view-sale-booths-id', 'Vista de taquillas', 'Vista de taquillas', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('view-payment-booths-id', 'Vista de liquidaciones', 'Vista de liquidaciones', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('check-ticket-id', 'Consultar boleto', 'Consultar boleto', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('cancel-ticket-id', 'Cancelar boleto', 'Cancelar boleto', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('print-ticket-id', 'Imprimir boletos', 'Imprimir boletos', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('buy-electronic-card-id', 'Compra y recarga de monedero electrónico', 'Compra y recarga de monedero electrónico', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('check-electronic-card-id', 'Consulta de monedero electrónico', 'Consulta de monedero electrónico', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('baggage-sale-id', 'Venta de equipaje (paquetería)', 'Venta de equipaje (paquetería)', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('generate-boarding-guide-id', 'Generar gúia de viaje y lista de abordar', 'Generar gúia de viaje y lista de abordar', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('print-boarding-list-id', 'Imprimir lista de abordar', 'Imprimir lista de abordar', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('debt-id', 'Adeudos', 'Adeudos', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('payments-id', 'Liquidaciones', 'Liquidaciones', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('weekly-payment-id', 'Pagos semanales', 'Pagos semanales', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('only-assign-operators-id', 'Asignación de operadores (sin mover autobuses)', 'Asignación de operadores (sin mover autobuses)', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('only-move-operators-id', 'Asignación de operadores (mover autobuses)', 'Asignación de operadores (mover autobuses)', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('view-map-id', 'Vista de autobuses', 'Vista de autobuses', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('view-route-id', 'Asignación de rutas (sólo ver)', 'Asignación de rutas (sólo ver)', current_timestamp, current_timestamp, 0);

INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('general-admin-role', 'Administración general', 'Administración general', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('admin-role', 'Administración', 'Administración general', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('logistic-role', 'Logística', 'Logística', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('human-resources-role', 'Recursos humanos', 'Recursos humanos', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('operator-human-resources-role', 'Recursos humanos operadores', 'Recursos humanos', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('sales-booth-role', 'Taquilla', 'Taquilla', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('sales-boss-role', 'Jefe de Taquilla', 'Jefe de Taquilla', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('payment-role', 'Liquidador', 'Liquidador', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('payment-boss-role', 'Jefe de liquidación', 'Jefe de liquidación', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('accounting-role', 'Contabilidad', 'Contabilidad', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('operator-chief-role', 'Jefe de operadores', 'Jefe de operadores', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('tracking-role', 'Rastreo', 'Rastreo', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('zone-supervisor-role', 'Supervisor Zona', 'Supervisor Zona', true, current_timestamp, current_timestamp, 0, null, 0);
INSERT INTO role(id, name, description, active, date_created, last_updated, version, redirect_url, redirect_url_order) VALUES ('maintenance-manager-role', 'Gerente de mantenimiento', 'Gerente de mantenimiento', true, current_timestamp, current_timestamp, 0, null, 0);

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-vehicle-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-default-vehicle-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-route-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'block-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-prices-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'occupation-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'join-trip-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'schedule-route-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'assign-buses-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'assign-operators-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-users-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-employee-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'view-sale-booths-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'view-payment-booths-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'check-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'cancel-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'print-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'buy-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'check-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'baggage-sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'generate-boarding-guide-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'print-boarding-list-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'debt-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'payments-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'weekly-payment-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'only-assign-operators-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'only-move-operators-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'view-map-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'view-route-id');


INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('admin-role', 'manage-vehicle-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('admin-role', 'manage-default-vehicle-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('admin-role', 'manage-route-id');


INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'block-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'manage-route-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'occupation-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'join-trip-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'schedule-route-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'assign-buses-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('logistic-role', 'assign-operators-id');


INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('human-resources-role', 'manage-users-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('human-resources-role', 'manage-employee-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('operator-human-resources-role', 'manage-employee-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'view-sale-booths-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'check-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'cancel-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'print-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'buy-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'check-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'baggage-sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'generate-boarding-guide-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-boss-role', 'print-boarding-list-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'check-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'cancel-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'print-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'buy-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'check-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'baggage-sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'generate-boarding-guide-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('sales-booth-role', 'print-boarding-list-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('payment-boss-role', 'view-payment-booths-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('payment-boss-role', 'payments-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('payment-boss-role', 'debt-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('payment-role', 'payments-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('accounting-role', 'weekly-payment-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('operator-chief-role', 'only-assign-operators-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('tracking-role', 'assign-operators-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('tracking-role', 'view-map-id');


INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'view-sale-booths-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'check-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'cancel-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'print-ticket-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'buy-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'check-electronic-card-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'baggage-sale-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'generate-boarding-guide-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'print-boarding-list-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'occupation-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('zone-supervisor-role', 'view-route-id');

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('maintenance-manager-role', 'only-move-operators-id');

INSERT INTO tickets_user_role(user_roles_id, role_id) VALUES('test-iamedu', 'general-admin-role');
INSERT INTO tickets_user_role(user_roles_id, role_id) VALUES('fae775ac-5a6f-4c67-9531-b527297a7e0a', 'general-admin-role');
