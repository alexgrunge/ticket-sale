CREATE TABLE social_data_provider (
	id VARCHAR(255) NOT NULL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	version bigint NOT NULL,
	date_created TIMESTAMP WITH TIME ZONE NOT NULL,
	last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE social_data (
	id VARCHAR(255) NOT NULL PRIMARY KEY,
	token_id VARCHAR(2048) NOT NULL,
	user_id VARCHAR(255) NOT NULL REFERENCES tickets_user(id),
	social_data_provider_id VARCHAR(255) NOT NULL REFERENCES social_data_provider(id),
	version bigint NOT NULL,
	date_created TIMESTAMP WITH TIME ZONE NOT NULL,
	last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
	UNIQUE (user_id),
	UNIQUE (token_id, social_data_provider_id)
);

INSERT INTO social_data_provider(id, name, version, date_created, last_updated) VALUES ('google-id', 'Google', 0, current_timestamp, current_timestamp);
INSERT INTO social_data_provider(id, name, version, date_created, last_updated) VALUES ('facebook-id', 'Facebook', 0, current_timestamp, current_timestamp);
