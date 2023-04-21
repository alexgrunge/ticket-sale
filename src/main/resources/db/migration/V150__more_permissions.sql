INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('ticket-sales', 'Venta de boleto', 'Venta de boleto', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('baggage-send', 'Enviar equipaje', 'Enviar equipaje', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('ticket-search', 'Busqueda de tickets', 'Busqueda de tickets', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('trip-guide', 'Guía de viaje', 'Guía de viaje', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('buy-card', 'Comprar tarjeta', 'Comprar tarjeta', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('cancel-trip', 'Cancelar viaje', 'Cancelar viaje', current_timestamp, current_timestamp, 0);

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'ticket-sales');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'baggage-send');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'ticket-search');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'trip-guide');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'buy-card');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('oficina', 'cancel-trip');

