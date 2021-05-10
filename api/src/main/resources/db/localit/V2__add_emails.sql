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
-- INSERT DATA
UPDATE users SET email = 'user1@example.com' WHERE id = '41096c69-47db-4fdb-9a84-bef10e571546';
UPDATE users SET email = 'user2@example.com' WHERE id = 'b72cfbb3-565d-4711-81ad-e6cdf1f349c1';
INSERT INTO users_roles(user_id, role_id) VALUES('41096c69-47db-4fdb-9a84-bef10e571546', 2);
INSERT INTO users_roles(user_id, role_id) VALUES('b72cfbb3-565d-4711-81ad-e6cdf1f349c1', 2);
INSERT INTO users(id, password, username, email) VALUES('d0c956e1-05bb-4a45-9213-98c5208ac2b8', '$argon2id$v=19$m=4096,t=3,p=1$HY1vJTI32xnLu2cZtL5i8g$T2lybUinI1cV10WgCfAActshXoLpzuvM/iu9jLF70Vg', 'unverified', 'unverified@example.com');
INSERT INTO users_roles(user_id, role_id) VALUES('d0c956e1-05bb-4a45-9213-98c5208ac2b8', 1);
