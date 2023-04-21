
CREATE TABLE bank (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    active boolean NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL
);


CREATE TABLE beneficiary (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    individual_id character varying(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    percentage numeric(19,2),
    type_relationship character varying(255) NOT NULL
);

CREATE TABLE card_data (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    account_number character varying(255),
    active boolean NOT NULL,
    bank_id character varying(255) NOT NULL,
    card_number character varying(255) NOT NULL,
    card_type character varying(255) NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL
);

CREATE TABLE department (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    active boolean NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description character varying(255),
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL
);

CREATE TABLE driver_license (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    antiqueness timestamp without time zone NOT NULL,
    date_created timestamp without time zone NOT NULL,
    expedition timestamp without time zone NOT NULL,
    expiration timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    license_number character varying(255) NOT NULL,
    license_type character varying(255) NOT NULL
);


CREATE TABLE employee_card_data (
    employee_cards_id character varying(255),
    card_data_id character varying(255)
);


CREATE TABLE employee_employee_event (
    employee_employee_events_id character varying(255),
    employee_event_id character varying(255)
);

CREATE TABLE employee_event (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    comment character varying(255) NOT NULL,
    date_created bytea NOT NULL,
    employee_position_id character varying(255) NOT NULL,
    end_date timestamp without time zone NOT NULL,
    event_type_id character varying(255) NOT NULL,
    last_updated bytea NOT NULL,
    start_date timestamp without time zone NOT NULL
);

CREATE TABLE employee_phone (
    employee_phones_id character varying(255),
    phone_id character varying(255)
);

CREATE TABLE employee_position (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    department_id character varying(255),
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL
);

CREATE TABLE event_type (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL
);

CREATE TABLE individual (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    address_id character varying(255),
    curp character varying(255),
    date_created timestamp without time zone NOT NULL,
    last_name character varying(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    rfc character varying(255),
    second_last_name character varying(255) NOT NULL,
    user_id character varying(255),
    class character varying(255) NOT NULL,
    driver_license_id character varying(255),
    email character varying(255),
    employee_photo character varying(255),
    employee_position_id character varying(255),
    employee_type character varying(255),
    job_detail_id character varying(255),
    personal_data_id character varying(255)
);

CREATE TABLE job_detail (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    base_salary numeric(19,2) NOT NULL,
    daily_wage numeric(19,2),
    date_created timestamp without time zone NOT NULL,
    job_name character varying(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    taxes_salary_journal numeric(19,2),
    top_salary numeric(19,2)
);

CREATE TABLE personal_data (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    birth_date timestamp without time zone,
    creditinfonavit boolean NOT NULL,
    date_created timestamp without time zone NOT NULL,
    enrollmentimss character varying(255),
    last_updated timestamp without time zone NOT NULL,
    marital_status character varying(255) NOT NULL,
    state_birth_id character varying(255),
    umf character varying(255)
);

CREATE TABLE phone (
    id character varying(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    number character varying(255) NOT NULL,
    type character varying(255) NOT NULL
);

CREATE TABLE address (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    address VARCHAR(255) NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    municipality_id VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    settlement_id VARCHAR(255) NOT NULL,
    state_id VARCHAR(255) NOT NULL
);

CREATE TABLE bus (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description VARCHAR(255),
    last_updated timestamp without time zone NOT NULL,
    plates VARCHAR(255) NOT NULL,
    service_type_id VARCHAR(255)
);


CREATE TABLE bus_bus_position (
    bus_positions_id VARCHAR(255),
    bus_position_id VARCHAR(255),
    bus_seats_id VARCHAR(255)
);

CREATE TABLE coupon (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    amount numeric(19,2) NOT NULL,
    date_created timestamp without time zone NOT NULL,
    folio VARCHAR(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    status VARCHAR(255) NOT NULL,
    used_date bytea NOT NULL,
    valid_to bytea,
    class VARCHAR(255) NOT NULL,
    run_id VARCHAR(255)
);


CREATE TABLE municipality (
    id VARCHAR(100),
    name VARCHAR(255) NOT NULL,
    state_id VARCHAR(100) NOT NULL,
    date_created timestamp,
    last_updated timestamp,
    version BIGINT NOT NULL
);


CREATE TABLE payment (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    amount numeric(19,2) NOT NULL,
    coupon_id VARCHAR(255),
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    paying_method VARCHAR(255) NOT NULL
);


CREATE TABLE permission (
    id VARCHAR(100),
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(512),
    date_created timestamp,
    last_updated timestamp,
    version BIGINT NOT NULL
);


CREATE TABLE physical_sale_point_employee (
    physical_sale_point_employees_id VARCHAR(255),
    employee_id VARCHAR(255)
);


CREATE TABLE product (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description VARCHAR(255),
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    class VARCHAR(255) NOT NULL,
    active boolean,
    beginning_id VARCHAR(255),
    destination_id VARCHAR(255),
    route_id VARCHAR(255),
    valid_from bytea,
    valid_to bytea
);


CREATE TABLE product_category (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    active boolean NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description VARCHAR(255),
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    parent_id VARCHAR(255)
);


CREATE TABLE product_price (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    price numeric(19,2) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    valid_from timestamp without time zone NOT NULL,
    valid_to timestamp without time zone
);


CREATE TABLE product_product_category (
    product_categories_id VARCHAR(255),
    product_category_id VARCHAR(255)
);


CREATE TABLE refund (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    amount numeric(19,2) NOT NULL,
    coupon_id VARCHAR(255),
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    type VARCHAR(255) NOT NULL
);


CREATE TABLE reservation (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    item_quantity bigint NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    sale_point_id VARCHAR(255) NOT NULL,
    salesman_id VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    subtotal numeric(19,2) NOT NULL,
    total numeric(19,2) NOT NULL
);


CREATE TABLE reservation_sale_item (
    reservation_items_id VARCHAR(255),
    sale_item_id VARCHAR(255)
);


CREATE TABLE role (
    id VARCHAR(100),
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(512),
    active BOOLEAN DEFAULT true,
    date_created timestamp,
    last_updated timestamp,
    version BIGINT NOT NULL
);


CREATE TABLE role_permission (
    role_permissions_id VARCHAR(255),
    permission_id VARCHAR(255)
);


CREATE TABLE route (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    beginning_id VARCHAR(255) NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description VARCHAR(255),
    destination_id VARCHAR(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    total_kilometers numeric(19,2) NOT NULL
);


CREATE TABLE run_service_type_time (
    run_service_type_times_id VARCHAR(255),
    service_type_time_id VARCHAR(255)
);


CREATE TABLE run_stop_off (
    run_stops_id VARCHAR(255),
    stop_off_id VARCHAR(255)
);


CREATE TABLE sale (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    folio VARCHAR(255) NOT NULL,
    item_quantity bigint NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    reservation_id VARCHAR(255),
    sale_point_id VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL
);


CREATE TABLE sale_discount (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    active boolean NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    percentage numeric(19,2),
    rule_script_id VARCHAR(255),
    type VARCHAR(255) NOT NULL
);


CREATE TABLE sale_item (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    product_price_id VARCHAR(255) NOT NULL,
    quantity bigint NOT NULL,
    class VARCHAR(255) NOT NULL,
    boarding_comment VARCHAR(255),
    boarding_stop_id VARCHAR(255),
    folio VARCHAR(255),
    leaving_comment VARCHAR(255),
    leaving_stop_id VARCHAR(255),
    seat_id VARCHAR(255),
    status VARCHAR(255),
    trip_id VARCHAR(255)
);


CREATE TABLE sale_item_sale_discount (
    sale_item_discounts_id VARCHAR(255),
    sale_discount_id VARCHAR(255)
);


CREATE TABLE sale_participant (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    employee_id VARCHAR(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    position_id VARCHAR(255) NOT NULL
);


CREATE TABLE sale_payment (
    sale_payments_id VARCHAR(255),
    payment_id VARCHAR(255)
);


CREATE TABLE sale_point (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    active boolean NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    class VARCHAR(255) NOT NULL,
    address_id VARCHAR(255),
    latitude numeric(19,2),
    longitude numeric(19,2),
    domain VARCHAR(255)
);


CREATE TABLE sale_refund (
    sale_refunds_id VARCHAR(255),
    refund_id VARCHAR(255)
);


CREATE TABLE sale_sale_discount (
    sale_discounts_id VARCHAR(255),
    sale_discount_id VARCHAR(255)
);


CREATE TABLE sale_sale_item (
    sale_item_history_id VARCHAR(255),
    sale_item_id VARCHAR(255),
    sale_items_id VARCHAR(255)
);


CREATE TABLE sale_sale_participant (
    sale_participants_id VARCHAR(255),
    sale_participant_id VARCHAR(255)
);


CREATE TABLE sale_tax (
    sale_taxes_id VARCHAR(255),
    tax_id VARCHAR(255)
);


CREATE TABLE script (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    body VARCHAR(255) NOT NULL,
    category_id VARCHAR(255),
    date_created timestamp without time zone NOT NULL,
    description VARCHAR(255),
    language VARCHAR(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    variables VARCHAR(255)
);


CREATE TABLE script_category (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL
);


CREATE TABLE script_role (
    script_roles_id VARCHAR(255),
    role_id VARCHAR(255)
);


CREATE TABLE service_type (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL
);


CREATE TABLE service_type_time (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    departure_time time without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    service_type_id VARCHAR(255) NOT NULL
);


CREATE TABLE settlement (
    id VARCHAR(100),
    name VARCHAR(255) NOT NULL,
    postal_code VARCHAR(100) NOT NULL,
    municipality_id VARCHAR(100) NOT NULL,
    state_id VARCHAR(100) NOT NULL,
    date_created timestamp,
    last_updated timestamp,
    version BIGINT NOT NULL
);


CREATE TABLE state (
    id VARCHAR(100),
    name VARCHAR(255) NOT NULL UNIQUE,
    iso_code VARCHAR(6) NOT NULL UNIQUE,
    date_created timestamp,
    last_updated timestamp,
    version BIGINT NOT NULL
);


CREATE TABLE tax (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name VARCHAR(255) NOT NULL,
    percentage numeric(19,2) NOT NULL
);


CREATE TABLE tickets_user (
    id VARCHAR(100),
    name VARCHAR(300) NOT NULL,
    last_name VARCHAR(300) NOT NULL,
    second_last_name VARCHAR(300),
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    date_created timestamp,
    last_updated timestamp,
    version BIGINT NOT NULL
);


CREATE TABLE tickets_user_role (
    user_roles_id VARCHAR(255),
    role_id VARCHAR(255)
);


CREATE TABLE trip (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    bus_id VARCHAR(255),
    date_created timestamp without time zone NOT NULL,
    departure_date timestamp without time zone NOT NULL,
    driver_id VARCHAR(255),
    last_updated timestamp without time zone NOT NULL,
    run_id VARCHAR(255) NOT NULL,
    service_type_id VARCHAR(255) NOT NULL,
    sold_tickets integer NOT NULL
);


CREATE TABLE trip_seat (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    seat_id VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL
);


CREATE TABLE trip_seat_event (
    id VARCHAR(255) NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    employee_id VARCHAR(255),
    last_updated timestamp without time zone NOT NULL,
    seat_id VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL
);


CREATE TABLE trip_trip_seat (
    trip_seats_id VARCHAR(255),
    trip_seat_id VARCHAR(255)
);


ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);


ALTER TABLE ONLY beneficiary
    ADD CONSTRAINT beneficiary_pkey PRIMARY KEY (id);


ALTER TABLE ONLY card_data
    ADD CONSTRAINT card_data_pkey PRIMARY KEY (id);


ALTER TABLE ONLY department
    ADD CONSTRAINT department_pkey PRIMARY KEY (id);

ALTER TABLE ONLY driver_license
    ADD CONSTRAINT driver_license_pkey PRIMARY KEY (id);


ALTER TABLE ONLY employee_event
    ADD CONSTRAINT employee_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY employee_position
    ADD CONSTRAINT employee_position_pkey PRIMARY KEY (id);

ALTER TABLE ONLY event_type
    ADD CONSTRAINT event_type_pkey PRIMARY KEY (id);


ALTER TABLE ONLY individual
    ADD CONSTRAINT individual_pkey PRIMARY KEY (id);

ALTER TABLE ONLY job_detail
    ADD CONSTRAINT job_detail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY personal_data
    ADD CONSTRAINT personal_data_pkey PRIMARY KEY (id);

ALTER TABLE ONLY phone
    ADD CONSTRAINT phone_pkey PRIMARY KEY (id);

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bus
    ADD CONSTRAINT bus_pkey PRIMARY KEY (id);


ALTER TABLE ONLY coupon
    ADD CONSTRAINT coupon_pkey PRIMARY KEY (id);


ALTER TABLE ONLY municipality
    ADD CONSTRAINT municipality_pkey PRIMARY KEY (id);


ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


ALTER TABLE ONLY product_category
    ADD CONSTRAINT product_category_pkey PRIMARY KEY (id);


ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);

ALTER TABLE ONLY product_price
    ADD CONSTRAINT product_price_pkey PRIMARY KEY (id);


ALTER TABLE ONLY refund
    ADD CONSTRAINT refund_pkey PRIMARY KEY (id);

ALTER TABLE ONLY reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY route
    ADD CONSTRAINT route_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sale_discount
    ADD CONSTRAINT sale_discount_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sale_item
    ADD CONSTRAINT sale_item_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sale_participant
    ADD CONSTRAINT sale_participant_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sale_point
    ADD CONSTRAINT sale_point_pkey PRIMARY KEY (id);

ALTER TABLE ONLY script_category
    ADD CONSTRAINT script_category_pkey PRIMARY KEY (id);

ALTER TABLE ONLY script
    ADD CONSTRAINT script_pkey PRIMARY KEY (id);

ALTER TABLE ONLY service_type
    ADD CONSTRAINT service_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY service_type_time
    ADD CONSTRAINT service_type_time_pkey PRIMARY KEY (id);

ALTER TABLE ONLY settlement
    ADD CONSTRAINT settlement_pkey PRIMARY KEY (id);

ALTER TABLE ONLY state
    ADD CONSTRAINT state_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tax
    ADD CONSTRAINT tax_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tickets_user
    ADD CONSTRAINT tickets_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY trip
    ADD CONSTRAINT trip_pkey PRIMARY KEY (id);

ALTER TABLE ONLY trip_seat_event
    ADD CONSTRAINT trip_seat_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY trip_seat
    ADD CONSTRAINT trip_seat_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tickets_user
    ADD CONSTRAINT uk_ie1j27f0m5p2s7fwd87wxnlul UNIQUE (username);


ALTER TABLE ONLY employee_phone
    ADD CONSTRAINT fk_1047elmbetr6dufd19avjsnfv FOREIGN KEY (phone_id) REFERENCES phone(id);

ALTER TABLE ONLY beneficiary
    ADD CONSTRAINT fk_30gaud0l43lyqke7h2ugru26q FOREIGN KEY (individual_id) REFERENCES individual(id);

ALTER TABLE ONLY employee_card_data
    ADD CONSTRAINT fk_3a35vdqxwxspyquy4893y1vrl FOREIGN KEY (employee_cards_id) REFERENCES individual(id);

ALTER TABLE ONLY employee_event
    ADD CONSTRAINT fk_3v00p478dal6t2s90w3f5mqoj FOREIGN KEY (employee_position_id) REFERENCES employee_position(id);

ALTER TABLE ONLY employee_card_data
    ADD CONSTRAINT fk_9up0joh43m1psv7uw2rp4f6pc FOREIGN KEY (card_data_id) REFERENCES card_data(id);

ALTER TABLE ONLY individual
    ADD CONSTRAINT fk_c7duq7ej12muu0gilsmsonfat FOREIGN KEY (employee_position_id) REFERENCES employee_position(id);

ALTER TABLE ONLY individual
    ADD CONSTRAINT fk_ebx01ero4h4fcjpf9qii0hm9l FOREIGN KEY (personal_data_id) REFERENCES personal_data(id);

ALTER TABLE ONLY individual
    ADD CONSTRAINT fk_g6yfd8qhuokai7vj6f3ew957v FOREIGN KEY (job_detail_id) REFERENCES job_detail(id);

ALTER TABLE ONLY employee_employee_event
    ADD CONSTRAINT fk_i0o0v4pu4osk8ehuad9aq8ini FOREIGN KEY (employee_employee_events_id) REFERENCES individual(id);

ALTER TABLE ONLY employee_position
    ADD CONSTRAINT fk_jmjvp2gtf7jbvkjj20xm2odmi FOREIGN KEY (department_id) REFERENCES department(id);

ALTER TABLE ONLY employee_event
    ADD CONSTRAINT fk_k1vu7s9an7shjqt032d3s5b65 FOREIGN KEY (event_type_id) REFERENCES event_type(id);

ALTER TABLE ONLY card_data
    ADD CONSTRAINT fk_odt4kr643wxa93qh9ucdcj26 FOREIGN KEY (bank_id) REFERENCES bank(id);

ALTER TABLE ONLY employee_employee_event
    ADD CONSTRAINT fk_py1keokeb79xh9vmknslqd9m4 FOREIGN KEY (employee_event_id) REFERENCES employee_event(id);

ALTER TABLE ONLY individual
    ADD CONSTRAINT fk_tfj1cfsssy8nmdb07h3wev5bc FOREIGN KEY (driver_license_id) REFERENCES driver_license(id);

ALTER TABLE ONLY sale_participant
    ADD CONSTRAINT fk_1i4wxphg5s3hllv9t2x03fklm FOREIGN KEY (employee_id) REFERENCES individual(id);

ALTER TABLE ONLY run_stop_off
    ADD CONSTRAINT fk_1t9a874sf7aie9u1ljhpl3wf6 FOREIGN KEY (run_stops_id) REFERENCES product(id);

ALTER TABLE ONLY trip
    ADD CONSTRAINT fk_1yfp1tjg94qvt8efh9unq90rb FOREIGN KEY (bus_id) REFERENCES bus(id);

ALTER TABLE ONLY sale_item
    ADD CONSTRAINT fk_393r3cboi7glp3pn7dthkvxn4 FOREIGN KEY (product_id) REFERENCES product(id);

ALTER TABLE ONLY settlement
    ADD CONSTRAINT fk_4evr7es1huw0m75lvj8h4ydh FOREIGN KEY (state_id) REFERENCES state(id);

ALTER TABLE ONLY address
    ADD CONSTRAINT fk_5hunxa972yemj6jvwx17xy270 FOREIGN KEY (settlement_id) REFERENCES settlement(id);

ALTER TABLE ONLY physical_sale_point_employee
    ADD CONSTRAINT fk_5lg0peu6s9q8rk25k8iik5hje FOREIGN KEY (physical_sale_point_employees_id) REFERENCES sale_point(id);

ALTER TABLE ONLY individual
    ADD CONSTRAINT fk_5pa6ks755lemh965k5xh5qi8o FOREIGN KEY (user_id) REFERENCES tickets_user(id);

ALTER TABLE ONLY bus_bus_position
    ADD CONSTRAINT fk_636vgmamhvymraes4d59myvv1 FOREIGN KEY (bus_seats_id) REFERENCES bus(id);

ALTER TABLE ONLY product_product_category
    ADD CONSTRAINT fk_6rxtsmf22779u88n8whlg3pct FOREIGN KEY (product_categories_id) REFERENCES product(id);

ALTER TABLE ONLY product_product_category
    ADD CONSTRAINT fk_7pbfnh5e3r12cggato8pt4a2x FOREIGN KEY (product_category_id) REFERENCES product_category(id);

ALTER TABLE ONLY bus_bus_position
    ADD CONSTRAINT fk_7u11mldr4dm4wen5fw63ll87m FOREIGN KEY (bus_positions_id) REFERENCES bus(id);

ALTER TABLE ONLY coupon
    ADD CONSTRAINT fk_7ufjp9idx1l7dowt7kjp4jx0h FOREIGN KEY (run_id) REFERENCES product(id);

ALTER TABLE ONLY sale_participant
    ADD CONSTRAINT fk_7xa6x57sy1a7jm23namnj0jpf FOREIGN KEY (position_id) REFERENCES employee_position(id);

ALTER TABLE ONLY script_role
    ADD CONSTRAINT fk_85cin2ji61ccfuh0y7i2ktaa3 FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE ONLY sale
    ADD CONSTRAINT fk_8y51ek10n9a18y64rmmjciea0 FOREIGN KEY (sale_point_id) REFERENCES sale_point(id);

ALTER TABLE ONLY trip
    ADD CONSTRAINT fk_94jkkk9h9qd0u4q7fg3cqeh0v FOREIGN KEY (service_type_id) REFERENCES service_type(id);

ALTER TABLE ONLY sale_item
    ADD CONSTRAINT fk_9h6pgxh16sd4seuye6nayqj8n FOREIGN KEY (product_price_id) REFERENCES product_price(id);

ALTER TABLE ONLY sale_item_sale_discount
    ADD CONSTRAINT fk_9jbrsrogpf37scv8yr0j6kuop FOREIGN KEY (sale_discount_id) REFERENCES sale_discount(id);

ALTER TABLE ONLY trip
    ADD CONSTRAINT fk_a2y1p9vb2hsfkgfq26y9gtksd FOREIGN KEY (driver_id) REFERENCES individual(id);

ALTER TABLE ONLY sale_discount
    ADD CONSTRAINT fk_a7ve6cja6jk47msuijf2gqx7p FOREIGN KEY (rule_script_id) REFERENCES script(id);

ALTER TABLE ONLY sale_refund
    ADD CONSTRAINT fk_apikeukc3mxxdkoo689cd2pbj FOREIGN KEY (refund_id) REFERENCES refund(id);

ALTER TABLE ONLY role_permission
    ADD CONSTRAINT fk_awm2aimiwlo75ay72sllny51p FOREIGN KEY (role_permissions_id) REFERENCES role(id);

ALTER TABLE ONLY reservation
    ADD CONSTRAINT fk_c3hhgnm6i827hqwoyyhr8enul FOREIGN KEY (salesman_id) REFERENCES sale_participant(id);

ALTER TABLE ONLY sale_sale_participant
    ADD CONSTRAINT fk_c5femlbg5pstolkhi5jtwklv FOREIGN KEY (sale_participants_id) REFERENCES sale(id);

ALTER TABLE ONLY run_service_type_time
    ADD CONSTRAINT fk_cab8pg1is734bbd3hn9fjew0q FOREIGN KEY (run_service_type_times_id) REFERENCES product(id);

ALTER TABLE ONLY individual
    ADD CONSTRAINT fk_coy93ty63r63vm1sak8btgenc FOREIGN KEY (address_id) REFERENCES address(id);

ALTER TABLE ONLY sale_item_sale_discount
    ADD CONSTRAINT fk_cqcmgel0x2awkmaqulwp751gj FOREIGN KEY (sale_item_discounts_id) REFERENCES sale_item(id);

ALTER TABLE ONLY reservation
    ADD CONSTRAINT fk_d9ydhpthbdnuo8eyhaii2oonn FOREIGN KEY (sale_point_id) REFERENCES sale_point(id);

ALTER TABLE ONLY sale_point
    ADD CONSTRAINT fk_de41ygplnkvjm33gmfj76rc0b FOREIGN KEY (address_id) REFERENCES address(id);

ALTER TABLE ONLY sale_payment
    ADD CONSTRAINT fk_dgx1h741v1y2592v8pq6ifc2g FOREIGN KEY (payment_id) REFERENCES payment(id);

ALTER TABLE ONLY reservation_sale_item
    ADD CONSTRAINT fk_e0xdibowrn0i285p3d30spx4m FOREIGN KEY (sale_item_id) REFERENCES sale_item(id);

ALTER TABLE ONLY product_price
    ADD CONSTRAINT fk_e9g64bcsjeqokr1a2g7e8ge7m FOREIGN KEY (product_id) REFERENCES product(id);

ALTER TABLE ONLY sale_sale_item
    ADD CONSTRAINT fk_ejhkfwncim3xln3fwuta86p7y FOREIGN KEY (sale_item_id) REFERENCES sale_item(id);

ALTER TABLE ONLY role_permission
    ADD CONSTRAINT fk_fn4pldu982p9u158rpk6nho5k FOREIGN KEY (permission_id) REFERENCES permission(id);

ALTER TABLE ONLY sale_sale_discount
    ADD CONSTRAINT fk_g42qyjyu1la6ukqwkbcpfng97 FOREIGN KEY (sale_discounts_id) REFERENCES sale(id);

ALTER TABLE ONLY sale_sale_item
    ADD CONSTRAINT fk_gchmbfkagmyf7gpm11fidl4es FOREIGN KEY (sale_items_id) REFERENCES sale(id);

ALTER TABLE ONLY address
    ADD CONSTRAINT fk_ged0l82f6pvqqe2we1yrhkflp FOREIGN KEY (municipality_id) REFERENCES municipality(id);

ALTER TABLE ONLY run_service_type_time
    ADD CONSTRAINT fk_genxdor1kq1hdvhphuyrai5k9 FOREIGN KEY (service_type_time_id) REFERENCES service_type_time(id);

ALTER TABLE ONLY product_category
    ADD CONSTRAINT fk_ghhfjaxmf8ui47rtl6uctuy0j FOREIGN KEY (parent_id) REFERENCES product_category(id);

ALTER TABLE ONLY product
    ADD CONSTRAINT fk_gosp8bfpgmqt01urka4hg6dhh FOREIGN KEY (route_id) REFERENCES route(id);

ALTER TABLE ONLY address
    ADD CONSTRAINT fk_gsutw0t9tnbyjepppx6e2473t FOREIGN KEY (state_id) REFERENCES state(id);

ALTER TABLE ONLY sale_item
    ADD CONSTRAINT fk_hapbbbji03xnofm9o56hp3304 FOREIGN KEY (trip_id) REFERENCES trip(id);

ALTER TABLE ONLY sale_sale_discount
    ADD CONSTRAINT fk_hjpibmps61h59f666v4746onb FOREIGN KEY (sale_discount_id) REFERENCES sale_discount(id);

ALTER TABLE ONLY service_type_time
    ADD CONSTRAINT fk_hsjtqtpapnlhr8jrudv0tofuo FOREIGN KEY (service_type_id) REFERENCES service_type(id);

ALTER TABLE ONLY sale_tax
    ADD CONSTRAINT fk_hvmrcx57b01fm6w5qltxantgl FOREIGN KEY (sale_taxes_id) REFERENCES sale(id);

ALTER TABLE ONLY script_role
    ADD CONSTRAINT fk_kiww3l2bbh8q2oma4k9udxyt0 FOREIGN KEY (script_roles_id) REFERENCES script(id);

ALTER TABLE ONLY settlement
    ADD CONSTRAINT fk_lbv5u947lk65wc5oku660t0mb FOREIGN KEY (municipality_id) REFERENCES municipality(id);

ALTER TABLE ONLY reservation_sale_item
    ADD CONSTRAINT fk_lfcsk319wpcyr9avdaacn6o4h FOREIGN KEY (reservation_items_id) REFERENCES reservation(id);

ALTER TABLE ONLY script
    ADD CONSTRAINT fk_m51xmehvs9nf1chijnvwehp56 FOREIGN KEY (category_id) REFERENCES script_category(id);

ALTER TABLE ONLY bus
    ADD CONSTRAINT fk_m5nsd2rwhrlijcrfxgevmy80n FOREIGN KEY (service_type_id) REFERENCES service_type(id);

ALTER TABLE ONLY trip_seat_event
    ADD CONSTRAINT fk_n2yait9065y46vxe23rkipgsg FOREIGN KEY (employee_id) REFERENCES individual(id);

ALTER TABLE ONLY sale_payment
    ADD CONSTRAINT fk_nknagbx3ei0jldk6flvmd32vi FOREIGN KEY (sale_payments_id) REFERENCES sale(id);

ALTER TABLE ONLY sale_refund
    ADD CONSTRAINT fk_nlt1roede3ffn7cuwfaddt23k FOREIGN KEY (sale_refunds_id) REFERENCES sale(id);

ALTER TABLE ONLY trip
    ADD CONSTRAINT fk_o70b84ahaijbiv0s198gr7x2i FOREIGN KEY (run_id) REFERENCES product(id);

ALTER TABLE ONLY sale_tax
    ADD CONSTRAINT fk_ob63059h5ok6ngbsdy6ngwqts FOREIGN KEY (tax_id) REFERENCES tax(id);

ALTER TABLE ONLY payment
    ADD CONSTRAINT fk_pi08r8p4tb653kwk9akairqlo FOREIGN KEY (coupon_id) REFERENCES coupon(id);

ALTER TABLE ONLY municipality
    ADD CONSTRAINT fk_q3edqbx34ltlnjk9o5v4e1cb FOREIGN KEY (state_id) REFERENCES state(id);

ALTER TABLE ONLY sale_sale_participant
    ADD CONSTRAINT fk_qgpmlqyhsjaxejtiqkkjq9wdg FOREIGN KEY (sale_participant_id) REFERENCES sale_participant(id);

ALTER TABLE ONLY sale_sale_item
    ADD CONSTRAINT fk_qw0cb5f61cfrey2umuujki2hx FOREIGN KEY (sale_item_history_id) REFERENCES sale(id);

ALTER TABLE ONLY physical_sale_point_employee
    ADD CONSTRAINT fk_rohb205ubqr5tvxf2ybnhb7pf FOREIGN KEY (employee_id) REFERENCES individual(id);

ALTER TABLE ONLY trip_trip_seat
    ADD CONSTRAINT fk_sce1vn9xwaabv1nlg7gflqn8u FOREIGN KEY (trip_seat_id) REFERENCES trip_seat(id);

ALTER TABLE ONLY tickets_user_role
    ADD CONSTRAINT fk_sr4d68lai26se0xvsf14dd3kj FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE ONLY refund
    ADD CONSTRAINT fk_st6c1tspkgfe10v5cihpeadxp FOREIGN KEY (coupon_id) REFERENCES coupon(id);

ALTER TABLE ONLY tickets_user_role
    ADD CONSTRAINT fk_sx1k5nc1s6jvvgy86f8ffoo54 FOREIGN KEY (user_roles_id) REFERENCES tickets_user(id);

ALTER TABLE ONLY trip_trip_seat
    ADD CONSTRAINT fk_t7h0w0gvx45s8kk8ga6qv3usy FOREIGN KEY (trip_seats_id) REFERENCES trip(id);

ALTER TABLE ONLY sale
    ADD CONSTRAINT fk_tq50ae1svqlpucu4plwhk3jda FOREIGN KEY (reservation_id) REFERENCES reservation(id);



INSERT INTO tickets_user VALUES('anonymous-id', 'Anonimo', 'Anonimo', NULL, 'anonymous@anonymous.com', 'anonymous', '$2a$10$ZocdZczMLN89AC7p6a3XouxN9.I.LSm2LZgLidwI1Vm0m70w.YMri', current_timestamp, current_timestamp, 0);
INSERT INTO tickets_user VALUES('test-iamedu', 'Eduardo', 'Díaz', 'Real', 'iamedu.test@gmail.com', 'iamedu.test', '$2a$10$Mio7zSJymaQX3A9VAzuWguqg7.Cm4DD.cpFUXDeiX4UpbJHCFsS3m', current_timestamp, current_timestamp, 0);

INSERT INTO role VALUES('anonymous-role', 'ROLE_ANONYMOUS', 'The role to use as anonymous users', true, current_timestamp, current_timestamp, 0);
INSERT INTO role VALUES('customer-role', 'ROLE_CUSTOMER', 'Customers have this role', true, current_timestamp, current_timestamp, 0);
INSERT INTO role VALUES('admin-role', 'ROLE_ADMIN', 'Admins have this role', true, current_timestamp, current_timestamp, 0);

INSERT INTO tickets_user_role VALUES ('anonymous-id', 'anonymous-role');
INSERT INTO tickets_user_role VALUES ('test-iamedu', 'admin-role');
