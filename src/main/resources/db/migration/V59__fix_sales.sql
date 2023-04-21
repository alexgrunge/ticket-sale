CREATE TABLE daily_cashout (
    id VARCHAR(255) NOT NULL,
    sales_terminal_id VARCHAR(255) NOT NULL REFERENCES sales_terminal(id),
    cashed_amount NUMERIC(21, 4) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE cashout ADD COLUMN daily_cashout_id VARCHAR(255) REFERENCES daily_cashout(id);
ALTER TABLE cash_entry ALTER sales_terminal_id DROP NOT NULL;
