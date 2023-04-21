ALTER TABLE product DROP COLUMN valid_from;
ALTER TABLE product DROP COLUMN valid_to;

ALTER TABLE product ADD COLUMN valid_from timestamp;
ALTER TABLE product ADD COLUMN valid_to timestamp;

CREATE TABLE service_type_time_days (
    service_type_time_id character varying(255),
    day_of_week character varying(255)
);

ALTER TABLE ONLY service_type_time_days
    ADD CONSTRAINT fk_empd0tybf30xowlh85kxokjs9 FOREIGN KEY (service_type_time_id) REFERENCES service_type_time(id);

