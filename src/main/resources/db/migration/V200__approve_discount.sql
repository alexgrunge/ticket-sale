INSERT INTO permission VALUES('approve-discount-id', 'Aprobar descuento', 'Este permiso es para aprovar descuentos',  current_timestamp, current_timestamp, 0);

INSERT INTO role_permission VALUES('admin-role', 'approve-discount-id');
INSERT INTO role_permission VALUES('sales-boss-role', 'approve-discount-id');
