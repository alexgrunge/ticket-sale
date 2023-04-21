INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-catalog-id', 'Administrar catálogo', 'Administrar catálogo', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('manage-role-id', 'Administrar roles', 'Administrar roles', current_timestamp, current_timestamp, 0);
INSERT INTO permission(id, name, description, date_created, last_updated, version) VALUES('developer-id', 'Desarrollador', 'Desarrollador', current_timestamp, current_timestamp, 0);

INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-catalog-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'manage-role-id');
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('general-admin-role', 'developer-id');
