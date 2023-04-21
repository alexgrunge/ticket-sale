CREATE TABLE segment_var (
    id character varying(255) NOT NULL PRIMARY KEY,
    route_id VARCHAR(255) NOT NULL REFERENCES route(id),
    starting_stop_id VARCHAR(255) NOT NULL REFERENCES stop_off(id),
    ending_stop_id VARCHAR(255) NOT NULL REFERENCES stop_off(id),
    numeric_var NUMERIC(10,4),
    string_var VARCHAR(255),
    var_type VARCHAR(1) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    CHECK ((numeric_var IS NOT NULL OR string_var IS NOT NULL) AND (numeric_var IS NULL OR string_var IS NULL)),
    CHECK ((var_type = 'N' AND numeric_var IS NOT NULL) OR (var_type = 'S' AND string_var IS NOT NULL))
);
