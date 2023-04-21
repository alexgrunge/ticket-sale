DELETE FROM role_permission WHERE role_permissions_id = 'liquidador';
INSERT INTO role_permission(role_permissions_id, permission_id) VALUES('liquidador', 'payments');
