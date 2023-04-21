CREATE TABLE promotion (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    variables text NOT NULL,
    script_id character varying(255) NOT NULL REFERENCES script(id),
    valid_from timestamp without time zone,
    valid_to timestamp without time zone,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL
);
