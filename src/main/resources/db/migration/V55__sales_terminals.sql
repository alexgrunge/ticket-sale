CREATE TABLE sales_terminal (
    id VARCHAR(255) NOT NULL,
    office_location_id VARCHAR(255) NOT NULL REFERENCES office_location(id),
    terminal_id VARCHAR(255) NOT NULL UNIQUE,
    current_amount NUMERIC(20, 4) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cash_entry (
    id VARCHAR(255) NOT NULL,
    amount NUMERIC(20, 4) NOT NULL,
    sales_terminal_id VARCHAR(255) NOT NULL REFERENCES sales_terminal(id),
    user_id VARCHAR(255) NOT NULL REFERENCES tickets_user(id),
    cashed_out BOOLEAN NOT NULL DEFAULT false,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cashout (
    id VARCHAR(255) NOT NULL,
    sales_terminal_id VARCHAR(255) NOT NULL REFERENCES sales_terminal(id),
    cashed_amount NUMERIC(20, 4) NOT NULL,
    last_cashout BOOLEAN NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY (id)
);

