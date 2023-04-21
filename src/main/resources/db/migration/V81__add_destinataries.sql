CREATE TABLE notification_cc (
    notification_id VARCHAR(255) REFERENCES notification(id),
    cc_string VARCHAR(256),
    UNIQUE(notification_id, cc_string)
);

CREATE TABLE notification_bcc (
    notification_id VARCHAR(255) REFERENCES notification(id),
    bcc_string VARCHAR(256),
    UNIQUE(notification_id, bcc_string)
);
