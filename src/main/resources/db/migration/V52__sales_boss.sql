INSERT INTO tickets_user VALUES('jefe-oficina', 'Jefe', 'Oficina', 'Oficina', 'jefe.oficina@gmail.com', 'oficina', '$2a$10$Mio7zSJymaQX3A9VAzuWguqg7.Cm4DD.cpFUXDeiX4UpbJHCFsS3m', current_timestamp, current_timestamp, 0);

INSERT INTO role VALUES('oficina', 'oficina', 'oficina', true, current_timestamp, current_timestamp, 0);

INSERT INTO tickets_user_role VALUES ('jefe-oficina', 'oficina');

INSERT INTO permission VALUES('office-boss-id', 'Jefe de oficina', 'Este permiso es jefe de oficina',  current_timestamp, current_timestamp, 0);

INSERT INTO role_permission VALUES('admin-role', 'office-boss-id');
INSERT INTO role_permission VALUES('oficina', 'office-boss-id');

