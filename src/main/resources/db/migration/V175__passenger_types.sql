CREATE TABLE passenger_type (
  id VARCHAR(255) PRIMARY KEY NOT NULL,
  name VARCHAR(255) NOT NULL
);

INSERT INTO passenger_type VALUES ('ADULT', 'Adulto');
INSERT INTO passenger_type VALUES ('STUDENT', 'Estudiante');
INSERT INTO passenger_type VALUES ('OLDER_ADULT', 'Adulto mayor');
INSERT INTO passenger_type VALUES ('CHILD', 'Niño');
INSERT INTO passenger_type VALUES ('INFANT', 'Infante');

ALTER TABLE trip_seat ADD CONSTRAINT passenger_type_fk FOREIGN KEY (passenger_type) REFERENCES passenger_type(id);
