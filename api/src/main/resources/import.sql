INSERT INTO ROLES(id, name) VALUES(0, 'ROLE_ADMIN');
INSERT INTO ROLES(id, name) VALUES(1,'ROLE_USER');
INSERT INTO USERS(id, password, username) VALUES('41096c69-47db-4fdb-9a84-bef10e571546', '$argon2id$v=19$m=4096,t=3,p=1$c4QnZmuUngMznjDfDzO3Lw$IfqhldhF+n45fKKydinT4l/K/IaLWBtp6SekT8I+WNA', 'user1');
INSERT INTO USERS(id, password, username) VALUES('b72cfbb3-565d-4711-81ad-e6cdf1f349c1', '$argon2id$v=19$m=4096,t=3,p=1$sA5LURP6jE/+fTJ6i3BjIQ$3tDbCo7Rj2umWR3o2h7BTvFO0O0TNHZDXSlwm3h86rw', 'user2');
INSERT INTO USERS_ROLES(user_id, role_id) VALUES('41096c69-47db-4fdb-9a84-bef10e571546', 1)
INSERT INTO USERS_ROLES(user_id, role_id) VALUES('b72cfbb3-565d-4711-81ad-e6cdf1f349c1', 1)