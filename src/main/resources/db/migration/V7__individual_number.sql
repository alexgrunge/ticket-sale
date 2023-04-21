DELETE FROM individual;

ALTER TABLE individual ADD employee_number VARCHAR(256);

CREATE UNIQUE INDEX individual_emp_num ON individual(employee_number);


