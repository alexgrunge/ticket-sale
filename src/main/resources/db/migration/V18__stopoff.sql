CREATE TABLE scale (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description character varying(255),
    last_updated timestamp without time zone NOT NULL,
    latitude numeric(25,8),
    longitude numeric(25,8),
    name character varying(255) NOT NULL
);

CREATE TABLE stop_off (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description character varying(255),
    kilometers numeric(19,2),
    last_updated timestamp without time zone NOT NULL,
    missing_minutes bigint,
    name character varying(255) NOT NULL,
    order_index bigint NOT NULL,
    route_id character varying(255) NOT NULL,
    scale_id character varying(255) NOT NULL,
    travel_minutes bigint
);

ALTER TABLE ONLY scale
    ADD CONSTRAINT scale_pkey PRIMARY KEY (id);

ALTER TABLE ONLY stop_off
    ADD CONSTRAINT stop_off_pkey PRIMARY KEY (id);

ALTER TABLE ONLY stop_off
    ADD CONSTRAINT fk_78dcxwah74febpm0dr62y8910 FOREIGN KEY (scale_id) REFERENCES scale(id);

ALTER TABLE ONLY stop_off
    ADD CONSTRAINT fk_lw5wbe3vi8w10k9gucyqdqho1 FOREIGN KEY (route_id) REFERENCES route(id);

ALTER TABLE ONLY run_stop_off
    ADD CONSTRAINT fk_mn7i87u4pio6or5m1wed4n3qw FOREIGN KEY (stop_off_id) REFERENCES stop_off(id);

ALTER TABLE ONLY route
    ADD CONSTRAINT fk_ncponp75bmpylyiup53gdgdgj FOREIGN KEY (beginning_id) REFERENCES stop_off(id);

ALTER TABLE ONLY route
    ADD CONSTRAINT fk_pytkhpfjdm9wm2499offrg8xh FOREIGN KEY (destination_id) REFERENCES stop_off(id);

ALTER TABLE ONLY product
    ADD CONSTRAINT fk_lsftw1j9o65t7tssrg2lfn4aw FOREIGN KEY (beginning_id) REFERENCES stop_off(id);

ALTER TABLE ONLY product
    ADD CONSTRAINT fk_4cfjpfiigx72t51ayouq5j95n FOREIGN KEY (destination_id) REFERENCES stop_off(id);

ALTER TABLE ONLY sale_item
    ADD CONSTRAINT fk_ysrf4vtri5awkwdd9umxywnd FOREIGN KEY (boarding_stop_id) REFERENCES stop_off(id);

ALTER TABLE ONLY sale_item
    ADD CONSTRAINT fk_f7nwp4nl2nf78h2h6l3pdpr59 FOREIGN KEY (leaving_stop_id) REFERENCES stop_off(id);

ALTER TABLE route ALTER COLUMN beginning_id DROP NOT NULL;
ALTER TABLE route ALTER COLUMN destination_id DROP NOT NULL;

