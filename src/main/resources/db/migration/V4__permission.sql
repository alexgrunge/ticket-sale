DELETE FROM tickets_user_role;
DELETE FROM role;
DELETE FROM tickets_user;

INSERT INTO permission VALUES('manage-user-id', 'Administrar Usuario', 'Este permiso te permite administrar usuarios',  current_timestamp, current_timestamp, 0);
INSERT INTO permission VALUES('manage-role-id', 'Administrar Roles', 'Este permiso te permite administrar roles',  current_timestamp, current_timestamp, 0);
INSERT INTO permission VALUES('manage-route-id', 'Administrar Rutas', 'Este permiso te permite administrar rurtas',  current_timestamp, current_timestamp, 0);
INSERT INTO permission VALUES('manage-driver-id', 'Administrar Chofer', 'Este permiso te permite administrar choferes',  current_timestamp, current_timestamp, 0);

INSERT INTO tickets_user VALUES('test-iamedu', 'Eduardo', 'Díaz', 'Real', 'iamedu.test@gmail.com', 'iamedu.test', '$2a$10$Mio7zSJymaQX3A9VAzuWguqg7.Cm4DD.cpFUXDeiX4UpbJHCFsS3m', current_timestamp, current_timestamp, 0);
INSERT INTO tickets_user VALUES('test-hrManager', 'Eduardo', 'Díaz', 'Real', 'hrmanager.test@gmail.com', 'hrmanager.test', '$2a$10$Mio7zSJymaQX3A9VAzuWguqg7.Cm4DD.cpFUXDeiX4UpbJHCFsS3m', current_timestamp, current_timestamp, 0);

INSERT INTO role VALUES('hr-manager-role', 'Recursos Humanos (Gerencia)', 'Rol encargado de administrar usuarios', true, current_timestamp, current_timestamp, 0);
INSERT INTO role VALUES('admin-role', 'Administrador General', 'Administrador del sistema', true, current_timestamp, current_timestamp, 0);
INSERT INTO role VALUES('client-role', 'Cliente', 'El usuario que compra boletos', true, current_timestamp, current_timestamp, 0);

INSERT INTO role_permission VALUES('admin-role', 'manage-user-id');
INSERT INTO role_permission VALUES('admin-role', 'manage-role-id');
INSERT INTO role_permission VALUES('admin-role', 'manage-route-id');
INSERT INTO role_permission VALUES('admin-role', 'manage-driver-id');

INSERT INTO role_permission VALUES('hr-manager-role', 'manage-user-id');

INSERT INTO tickets_user_role VALUES ('test-iamedu', 'admin-role');
INSERT INTO tickets_user_role VALUES ('test-hrManager', 'hr-manager-role');
