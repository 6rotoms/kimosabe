CREATE TABLE verification_codes (
    verification_code character varying(255) NOT NULL,
    user_id uuid
);
ALTER TABLE ONLY users
    ADD email character varying(255);
ALTER TABLE ONLY users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);
ALTER TABLE ONLY verification_codes
    ADD CONSTRAINT fka4qo6nts1xd94owirq5evcpda FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ONLY verification_codes
    ADD CONSTRAINT verification_codes_pkey PRIMARY KEY (verification_code);
-- INSERT ROLES
INSERT INTO ROLES(id, name) VALUES(2,'ROLE_VERIFIED');
