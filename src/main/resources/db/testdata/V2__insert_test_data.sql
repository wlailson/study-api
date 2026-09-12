-- ============================================================
-- ROLES
-- ============================================================

INSERT INTO tb_role (authority)
VALUES ('ROLE_CLIENT');

INSERT INTO tb_role (authority)
VALUES ('ROLE_ADMIN');


-- ============================================================
-- USERS
-- ============================================================

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('Maria Silva',
        'maria@gmail.com',
        '$2a$12$XNDYRKncWl1260k8BNBIdOC5BexfIu/fWscpjYbRL2xoLUVR/6KLu',
        '61996695658',
        '1997-11-01');

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('João Santos',
        'joao@gmail.com',
        '$2a$12$XNDYRKncWl1260k8BNBIdOC5BexfIu/fWscpjYbRL2xoLUVR/6KLu',
        '61991234567',
        '1995-05-15');

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('Ana Oliveira',
        'ana@gmail.com',
        '$2a$12$XNDYRKncWl1260k8BNBIdOC5BexfIu/fWscpjYbRL2xoLUVR/6KLu',
        '61999887766',
        '2000-08-20');

INSERT INTO tb_user (name, email, password, phone, birth_date)
VALUES ('Carlos Almeida',
        'carlos@gmail.com',
        '$2a$12$XNDYRKncWl1260k8BNBIdOC5BexfIu/fWscpjYbRL2xoLUVR/6KLu',
        '61997766554',
        '1992-03-10');


-- ============================================================
-- USER ROLES
-- ============================================================

INSERT INTO tb_user_role (user_id, role_id)
VALUES (1, 1);

INSERT INTO tb_user_role (user_id, role_id)
VALUES (2, 1);

INSERT INTO tb_user_role (user_id, role_id)
VALUES (3, 1);

INSERT INTO tb_user_role (user_id, role_id)
VALUES (4, 2);


-- ============================================================
-- SUBJECTS - MARIA
-- ============================================================

INSERT INTO tb_subject (name, user_id)
VALUES ('Java', 1);

INSERT INTO tb_subject (name, user_id)
VALUES ('Spring Boot', 1);

INSERT INTO tb_subject (name, user_id)
VALUES ('Banco de Dados', 1);

INSERT INTO tb_subject (name, user_id)
VALUES ('Docker', 1);

INSERT INTO tb_subject (name, user_id)
VALUES ('Git e GitHub', 1);


-- ============================================================
-- SUBJECTS - JOÃO
-- ============================================================

INSERT INTO tb_subject (name, user_id)
VALUES ('JavaScript', 2);

INSERT INTO tb_subject (name, user_id)
VALUES ('React', 2);

INSERT INTO tb_subject (name, user_id)
VALUES ('HTML e CSS', 2);

INSERT INTO tb_subject (name, user_id)
VALUES ('TypeScript', 2);


-- ============================================================
-- SUBJECTS - ANA
-- ============================================================

INSERT INTO tb_subject (name, user_id)
VALUES ('Python', 3);

INSERT INTO tb_subject (name, user_id)
VALUES ('Machine Learning', 3);

INSERT INTO tb_subject (name, user_id)
VALUES ('Estatística', 3);

INSERT INTO tb_subject (name, user_id)
VALUES ('Pandas', 3);


-- ============================================================
-- SUBJECTS - CARLOS
-- ============================================================

INSERT INTO tb_subject (name, user_id)
VALUES ('Java', 4);

INSERT INTO tb_subject (name, user_id)
VALUES ('Arquitetura de Software', 4);

INSERT INTO tb_subject (name, user_id)
VALUES ('Microsserviços', 4);

INSERT INTO tb_subject (name, user_id)
VALUES ('Cloud Computing', 4);


-- ============================================================
-- TOPICS - MARIA
-- ============================================================

INSERT INTO tb_topic (name, user_id)
VALUES ('Herança', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Polimorfismo', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Encapsulamento', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Interfaces', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Streams', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Collections', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Injeção de Dependência', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Relacionamentos JPA', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Mapeamento JPA', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Spring Security', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('JWT', 1);

INSERT INTO tb_topic (name, user_id)
VALUES ('Docker Compose', 1);


-- ============================================================
-- TOPICS - JOÃO
-- ============================================================

INSERT INTO tb_topic (name, user_id)
VALUES ('Promises e Async/Await', 2);

INSERT INTO tb_topic (name, user_id)
VALUES ('Componentes', 2);

INSERT INTO tb_topic (name, user_id)
VALUES ('Hooks', 2);

INSERT INTO tb_topic (name, user_id)
VALUES ('React Router', 2);

INSERT INTO tb_topic (name, user_id)
VALUES ('TypeScript Types', 2);

INSERT INTO tb_topic (name, user_id)
VALUES ('Flexbox', 2);


-- ============================================================
-- TOPICS - ANA
-- ============================================================

INSERT INTO tb_topic (name, user_id)
VALUES ('Listas e Dicionários', 3);

INSERT INTO tb_topic (name, user_id)
VALUES ('Funções', 3);

INSERT INTO tb_topic (name, user_id)
VALUES ('Orientação a Objetos', 3);

INSERT INTO tb_topic (name, user_id)
VALUES ('Regressão Linear', 3);

INSERT INTO tb_topic (name, user_id)
VALUES ('Regressão Logística', 3);

INSERT INTO tb_topic (name, user_id)
VALUES ('Distribuição Normal', 3);

INSERT INTO tb_topic (name, user_id)
VALUES ('DataFrame', 3);


-- ============================================================
-- TOPICS - CARLOS
-- ============================================================

INSERT INTO tb_topic (name, user_id)
VALUES ('Streams', 4);

INSERT INTO tb_topic (name, user_id)
VALUES ('Clean Architecture', 4);

INSERT INTO tb_topic (name, user_id)
VALUES ('SOLID', 4);

INSERT INTO tb_topic (name, user_id)
VALUES ('DDD', 4);

INSERT INTO tb_topic (name, user_id)
VALUES ('Event-Driven Architecture', 4);

INSERT INTO tb_topic (name, user_id)
VALUES ('API Gateway', 4);

INSERT INTO tb_topic (name, user_id)
VALUES ('AWS EC2', 4);


-- ============================================================
-- STUDY SESSIONS - MARIA
-- ============================================================

-- ============================================================
-- STUDY SESSIONS - MARIA
-- ============================================================

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 1, 1, 60, 10, '2026-09-07');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 1, 2, 90, 15, '2026-09-08');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 1, 3, 60, 10, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 1, 4, 90, 15, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 1, 5, 120, 20, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 1, 6, 60, 10, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 2, 7, 120, 20, '2026-09-08');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 2, 8, 90, 15, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 3, 9, 60, 10, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 2, 10, 90, 15, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 2, 11, 60, 10, '2026-09-11');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (1, 4, 12, 120, 20, '2026-09-11');


-- ============================================================
-- STUDY SESSIONS - JOÃO
-- ============================================================

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (2, 6, 13, 90, 15, '2026-09-06');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (2, 7, 14, 120, 20, '2026-09-07');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (2, 7, 15, 60, 10, '2026-09-08');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (2, 7, 16, 90, 15, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (2, 9, 17, 60, 10, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (2, 8, 18, 120, 20, '2026-09-11');


-- ============================================================
-- STUDY SESSIONS - ANA
-- ============================================================

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 10, 19, 60, 10, '2026-09-07');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 10, 20, 90, 15, '2026-09-08');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 10, 21, 120, 20, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 11, 22, 120, 20, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 11, 23, 90, 15, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 12, 24, 60, 10, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (3, 13, 25, 90, 15, '2026-09-11');


-- ============================================================
-- STUDY SESSIONS - CARLOS
-- ============================================================

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 14, 26, 90, 15, '2026-09-07');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 15, 27, 120, 20, '2026-09-08');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 15, 28, 90, 15, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 15, 29, 120, 20, '2026-09-09');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 16, 30, 90, 15, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 16, 31, 120, 20, '2026-09-10');

INSERT INTO tb_study_session
(user_id, subject_id, topic_id, duration_in_minutes, break_time_in_minutes, date)
VALUES (4, 16, 32, 60, 10, '2026-09-11');

-- ============================================================
-- REVISIONS - MARIA
-- ============================================================

-- SESSION 1 - Herança
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-07', '2026-09-07', 30, 5, 'COMPLETED', 1, 1);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-14', NULL, NULL, NULL, 'PENDING', 1, 1);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-21', NULL, NULL, NULL, 'PENDING', 1, 1);


-- SESSION 2 - Polimorfismo
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-08', '2026-09-08', 45, 10, 'COMPLETED', 2, 1);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-15', NULL, NULL, NULL, 'PENDING', 2, 1);


-- SESSION 3 - Encapsulamento
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-09', '2026-09-09', 30, 5, 'COMPLETED', 3, 1);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-16', NULL, NULL, NULL, 'PENDING', 3, 1);


-- SESSION 4 - Interfaces
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-12', NULL, NULL, NULL, 'PENDING', 4, 1);


-- SESSION 5 - Streams
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-10', '2026-09-10', 60, 10, 'COMPLETED', 5, 1);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-17', NULL, NULL, NULL, 'PENDING', 5, 1);


-- SESSION 7 - Injeção de Dependência
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-13', NULL, NULL, NULL, 'PENDING', 7, 1);


-- SESSION 8 - Relacionamentos JPA
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-14', NULL, NULL, NULL, 'PENDING', 8, 1);


-- SESSION 9 - Mapeamento JPA
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-09', '2026-09-09', 30, 5, 'COMPLETED', 9, 1);

-- ============================================================
-- REVISIONS - JOÃO
-- ============================================================

-- SESSION 13 - Promises e Async/Await
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-08', '2026-09-08', 45, 10, 'COMPLETED', 13, 2);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-15', NULL, NULL, NULL, 'PENDING', 13, 2);


-- SESSION 14 - Componentes
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-16', NULL, NULL, NULL, 'PENDING', 14, 2);


-- SESSION 15 - Hooks
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-17', NULL, NULL, NULL, 'PENDING', 15, 2);


-- SESSION 16 - React Router
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-10', '2026-09-10', 45, 10, 'COMPLETED', 16, 2);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-17', NULL, NULL, NULL, 'PENDING', 16, 2);


-- ============================================================
-- REVISIONS - ANA
-- ============================================================

-- SESSION 19 - Listas e Dicionários
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-08', '2026-09-08', 30, 5, 'COMPLETED', 19, 3);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-15', NULL, NULL, NULL, 'PENDING', 19, 3);


-- SESSION 20 - Funções
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-09', '2026-09-09', 45, 10, 'COMPLETED', 20, 3);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-16', NULL, NULL, NULL, 'PENDING', 20, 3);


-- SESSION 22 - Regressão Linear
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-18', NULL, NULL, NULL, 'PENDING', 22, 3);


-- SESSION 23 - Regressão Logística
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-19', NULL, NULL, NULL, 'PENDING', 23, 3);


-- ============================================================
-- REVISIONS - CARLOS
-- ============================================================

-- SESSION 26 - Streams
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-09', '2026-09-09', 45, 10, 'COMPLETED', 26, 4);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-16', NULL, NULL, NULL, 'PENDING', 26, 4);


-- SESSION 27 - Clean Architecture
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-18', NULL, NULL, NULL, 'PENDING', 27, 4);


-- SESSION 28 - SOLID
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-10', '2026-09-10', 45, 10, 'COMPLETED', 28, 4);

INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-17', NULL, NULL, NULL, 'PENDING', 28, 4);


-- SESSION 30 - Event-Driven Architecture
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-20', NULL, NULL, NULL, 'PENDING', 30, 4);


-- SESSION 31 - API Gateway
INSERT INTO tb_revision
(scheduled_date, completed_date, duration_in_minutes, break_time_in_minutes, status, session_id, user_id)
VALUES ('2026-09-21', NULL, NULL, NULL, 'PENDING', 31, 4);

