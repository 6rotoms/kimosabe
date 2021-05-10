CREATE TABLE groups (
    id character varying(255) NOT NULL,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    owner character varying(255),
    name character varying(255)
);
CREATE TABLE roles (
    id bigint NOT NULL,
    name character varying(255)
);
CREATE TABLE users (
    id uuid NOT NULL,
    age integer,
    biography text,
    gender character varying(255),
    last_login timestamp without time zone,
    location character varying(255),
    password character varying(255),
    username character varying(255)
);
CREATE TABLE users_groups (
    groups_id character varying(255) NOT NULL,
    user_id uuid NOT NULL
);
CREATE TABLE users_relationships (
    requester_id uuid NOT NULL,
    target_id uuid NOT NULL,
    relationship_status character varying(255)
);
CREATE TABLE users_roles (
    user_id uuid NOT NULL,
    role_id bigint NOT NULL
);
ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);
ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);
ALTER TABLE ONLY users_groups
    ADD CONSTRAINT users_groups_pkey PRIMARY KEY (groups_id, user_id);
ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE ONLY users_relationships
    ADD CONSTRAINT users_relationships_pkey PRIMARY KEY (requester_id, target_id);
ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);
ALTER TABLE ONLY users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ONLY users_relationships
    ADD CONSTRAINT fk4q0bx10ba5sfvgku3g2f624jv FOREIGN KEY (target_id) REFERENCES users(id);
ALTER TABLE ONLY users_groups
    ADD CONSTRAINT fkg6fu0mfuj9eqfd9aro1nc40nn FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ONLY users_relationships
    ADD CONSTRAINT fkiru287rq6n8tiamn86i5efewb FOREIGN KEY (requester_id) REFERENCES users(id);
ALTER TABLE ONLY users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES roles(id);
ALTER TABLE ONLY users_groups
    ADD CONSTRAINT fkjex8no6gj9undclnlyn9l52wm FOREIGN KEY (groups_id) REFERENCES groups(id);

-- INSERT ROLES
INSERT INTO ROLES(id, name) VALUES(0, 'ROLE_ADMIN');
INSERT INTO ROLES(id, name) VALUES(1,'ROLE_USER');
