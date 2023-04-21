INSERT INTO tickets_user VALUES('test-taquilla', 'Taquilla', 'Taquilla', 'Taquilla', 'iamedu.taquilla@gmail.com', 'taquilla', '$2a$10$Mio7zSJymaQX3A9VAzuWguqg7.Cm4DD.cpFUXDeiX4UpbJHCFsS3m', current_timestamp, current_timestamp, 0);

INSERT INTO role VALUES('taquilla', 'Taquilla', 'Taquilla', true, current_timestamp, current_timestamp, 0);

INSERT INTO tickets_user_role VALUES ('test-taquilla', 'taquilla');

INSERT INTO permission VALUES('sales-id', 'Ventas', 'Este permiso es para vender',  current_timestamp, current_timestamp, 0);

INSERT INTO role_permission VALUES('admin-role', 'sales-id');
INSERT INTO role_permission VALUES('taquilla', 'sales-id');
