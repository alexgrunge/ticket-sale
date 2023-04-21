ALTER TABLE script DROP COLUMN variables;
ALTER TABLE script DROP COLUMN body;
ALTER TABLE script DROP COLUMN description;

ALTER TABLE script ADD COLUMN body TEXT NOT NULL;
ALTER TABLE script ADD COLUMN variables TEXT;
ALTER TABLE script ADD COLUMN description TEXT;

INSERT INTO script_category(id, name, version, date_created, last_updated) VALUES ('promocion', 'Promoción / descuento', 0, current_timestamp, current_timestamp);

INSERT INTO script(id, name, language, description, category_id, body, variables, version, date_created, last_updated) VALUES ('compra-mayor', 'Compra mayor a x', 'groovy', 'Descuento a compras mayores a un valor tienen un descuento de un porcentaje', 'promocion', 'println "Hola mundo"', '{ "type": "object", "title": "Script Variables", "properties": { "minValue": { "title": "Valor mínimo", "type": "number" }, "percentage": { "title": "Porcentaje", "type": "number" } }, "required": [ "minValue", "percentage" ] }', 0, current_timestamp, current_timestamp);
INSERT INTO script(id, name, language, description, category_id, body, version, date_created, last_updated) VALUES ('descuento-menores', 'Descuento Menores', 'groovy', 'Los niños pagan menos', 'promocion', 'println "Hola mundo"', 0, current_timestamp, current_timestamp);


