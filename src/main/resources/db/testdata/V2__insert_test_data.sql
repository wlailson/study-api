-- ROLES
INSERT INTO tb_role (authority)
VALUES ('ROLE_CLIENT');

INSERT INTO tb_role (authority)
VALUES ('ROLE_ADMIN');


-- USERS
INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('Maria Silva', 'maria@gmail.com', '123456', '61996695658', '1997-11-01');

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('João Santos', 'joao@gmail.com', '123456', '61991234567', '1995-05-15');

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('Ana Oliveira', 'ana@gmail.com', '123456', '61999887766', '2000-08-20');

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('Carlos Almeida', 'carlos@gmail.com', '123456', '61997766554', '1992-03-10');


-- USER ROLES
INSERT INTO tb_user_role (user_id, role_id)
VALUES (1, 1);

INSERT INTO tb_user_role (user_id, role_id)
VALUES (2, 1);

INSERT INTO tb_user_role (user_id, role_id)
VALUES (3, 1);

INSERT INTO tb_user_role (user_id, role_id)
VALUES (4, 2);


-- SUBJECTS - MARIA
INSERT INTO tb_subject (name, user_id)
VALUES ('Java', 1);

INSERT INTO tb_subject (name, user_id)
VALUES ('Spring Boot', 1);

INSERT INTO tb_subject (name, user_id)
VALUES ('Banco de Dados', 1);


-- SUBJECTS - JOÃO
INSERT INTO tb_subject (name, user_id)
VALUES ('JavaScript', 2);

INSERT INTO tb_subject (name, user_id)
VALUES ('React', 2);

INSERT INTO tb_subject (name, user_id)
VALUES ('HTML e CSS', 2);


-- SUBJECTS - ANA
INSERT INTO tb_subject (name, user_id)
VALUES ('Python', 3);

INSERT INTO tb_subject (name, user_id)
VALUES ('Machine Learning', 3);


-- SUBJECTS - CARLOS
INSERT INTO tb_subject (name, user_id)
VALUES ('Java', 4);

INSERT INTO tb_subject (name, user_id)
VALUES ('Arquitetura de Software', 4);


-- STUDY SESSIONS - MARIA
INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (1, 1, 'Herança', 60, 10, 'COMPLETED',
     '2026-09-07 10:00:00', '2026-09-07 12:00:00');

INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (1, 1, 'Polimorfismo', 90, 15, 'COMPLETED',
     '2026-09-07 14:00:00', '2026-09-07 15:45:00');

INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (1, 2, 'Injeção de Dependência', 120, 20, 'COMPLETED',
     '2026-09-08 09:00:00', '2026-09-08 11:20:00');

INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (1, 3, 'Relacionamentos JPA', 60, 10, 'IN_PROGRESS',
     '2026-09-08 15:00:00', NULL);


-- STUDY SESSIONS - JOÃO
INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (2, 4, 'Promises e Async/Await', 90, 15, 'COMPLETED',
     '2026-09-07 09:00:00', '2026-09-07 10:45:00');

INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (2, 5, 'Componentes', 120, 20, 'COMPLETED',
     '2026-09-08 10:00:00', '2026-09-08 12:20:00');


-- STUDY SESSIONS - ANA
INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (3, 7, 'Listas e Dicionários', 60, 10, 'COMPLETED',
     '2026-09-07 08:00:00', '2026-09-07 09:10:00');

INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (3, 8, 'Regressão Linear', 120, 20, 'COMPLETED',
     '2026-09-08 14:00:00', '2026-09-08 16:20:00');


-- STUDY SESSIONS - CARLOS
INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (4, 9, 'Streams', 90, 15, 'COMPLETED',
     '2026-09-07 19:00:00', '2026-09-07 20:45:00');

INSERT INTO tb_study_session
(user_id, subject_id, topic, duration_in_minutes, break_time_in_minutes, status, created_date, finished_date)
VALUES
    (4, 10, 'Clean Architecture', 120, 20, 'COMPLETED',
     '2026-09-08 19:00:00', '2026-09-08 21:20:00');


-- REVISIONS
INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-07', 1);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-14', 1);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-21', 1);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-08', 2);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-15', 2);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-08', 3);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-15', 3);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-07', 5);

INSERT INTO tb_revision (date, session_id)
VALUES ('2026-09-14', 5);


-- GOALS
INSERT INTO tb_goal (title, start_date, end_date, user_id)
VALUES ('Aprender Java', '2026-09-07', '2026-09-13', 1);

INSERT INTO tb_goal (title, start_date, end_date, user_id)
VALUES ('Dominar Spring Boot', '2026-09-07', '2026-09-20', 1);

INSERT INTO tb_goal (title, start_date, end_date, user_id)
VALUES ('Estudar Front-end', '2026-09-07', '2026-09-14', 2);

INSERT INTO tb_goal (title, start_date, end_date, user_id)
VALUES ('Aprender Machine Learning', '2026-09-07', '2026-09-21', 3);

INSERT INTO tb_goal (title, start_date, end_date, user_id)
VALUES ('Arquitetura de Software', '2026-09-07', '2026-09-20', 4);


-- GOAL ITEMS
INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (300, 1, 1);

INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (240, 1, 2);

INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (180, 2, 2);

INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (240, 3, 4);

INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (180, 3, 5);

INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (240, 4, 8);

INSERT INTO tb_goal_item (target_in_minutes, goal_id, subject_id)
VALUES (300, 5, 10);