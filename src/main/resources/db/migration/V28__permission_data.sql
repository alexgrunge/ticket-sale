INSERT INTO permission VALUES('developer-id', 'Desarrollador', 'Este permiso te permite ser desarrollador',  current_timestamp, current_timestamp, 0);

INSERT INTO role_permission VALUES('admin-role', 'developer-id');
