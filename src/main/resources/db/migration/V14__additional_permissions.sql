INSERT INTO permission VALUES('manage-catalog-id', 'Administra catálogos generales', 'Este permiso es para catálogos generales',  current_timestamp, current_timestamp, 0);
INSERT INTO permission VALUES('manage-vehicle-id', 'Administra vehículos', 'Este permiso permite administrar vehículos',  current_timestamp, current_timestamp, 0);

UPDATE permission SET name = 'Administrar empleados', description = 'Este permiso permite administrar empleados' WHERE id = 'manage-driver-id';


