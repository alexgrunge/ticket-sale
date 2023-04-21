CREATE TABLE sale_log (
    id VARCHAR(255) NOT NULL,
    sale_log TEXT NOT NULL,
    successful BOOLEAN NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY (id)
);
