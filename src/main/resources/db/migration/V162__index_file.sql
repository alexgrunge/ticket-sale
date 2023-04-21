drop index if exists stop_idx;

create unique index stop_idx ON trip_stop_control(trip_id, stop_off_id);
