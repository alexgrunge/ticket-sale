ALTER TABLE social_data ADD COLUMN provider_id VARCHAR(255) NOT NULL;
CREATE UNIQUE INDEX ON social_data(provider_id, social_data_provider_id);
