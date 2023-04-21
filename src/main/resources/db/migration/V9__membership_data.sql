CREATE TABLE employee_beneficiary (
    employee_beneficiaries_id character varying(255),
    beneficiary_id character varying(255)
);

DELETE FROM beneficiary;

ALTER TABLE beneficiary DROP COLUMN individual_id;
ALTER TABLE beneficiary ADD COLUMN name character varying(255);
ALTER TABLE beneficiary ADD COLUMN last_name character varying(255);
ALTER TABLE beneficiary ADD COLUMN second_last_name character varying(255);

ALTER TABLE ONLY employee_beneficiary
    ADD CONSTRAINT employee_beneficiary_fk FOREIGN KEY (beneficiary_id) REFERENCES beneficiary(id);
