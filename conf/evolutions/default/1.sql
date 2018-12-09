# --- First database schema
# --- !Ups

SET IGNORECASE TRUE;

CREATE TABLE customer (
  id                     BIGINT       NOT NULL,
  name                   VARCHAR(255) NOT NULL,
  CONSTRAINT pk_customer PRIMARY KEY  (id)
);

CREATE TABLE project (
  id                    BIGINT       NOT NULL,
  name                  VARCHAR(255) NOT NULL,
  customer_id           BIGINT       NOT NULL,
  CONSTRAINT pk_project PRIMARY KEY  (id)
);

CREATE SEQUENCE customer_seq START WITH 1000;
CREATE SEQUENCE project_seq  START WITH 2000;

   ALTER TABLE project
ADD CONSTRAINT fk_project_customer
   FOREIGN KEY (customer_id)
    REFERENCES customer(id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

CREATE INDEX ix_project_customer ON project (customer_id);

CREATE TABLE pos (
  id                     BIGINT       NOT NULL,
  name                   VARCHAR(255) NOT NULL,
  CONSTRAINT pk_position PRIMARY KEY  (id)
);

CREATE TABLE employee (
  id                     BIGINT       NOT NULL,
  name                   VARCHAR(255) NOT NULL,
  position_id            BIGINT       NOT NULL,
  CONSTRAINT pk_employee PRIMARY KEY  (id)
);

CREATE SEQUENCE position_seq START WITH 3000;
CREATE SEQUENCE employee_seq START WITH 4000;

   ALTER TABLE employee
ADD CONSTRAINT fk_employee_position
   FOREIGN KEY (position_id)
     REFERENCES pos(id)
             ON DELETE RESTRICT
             ON UPDATE RESTRICT;

CREATE INDEX ix_employee_position ON employee (position_id);

CREATE TABLE status (
  id                   BIGINT       NOT NULL,
  name                 VARCHAR(255) NOT NULL,
  CONSTRAINT pk_status PRIMARY KEY  (id)
);

CREATE TABLE priority (
  id                     BIGINT       NOT NULL,
  name                   VARCHAR(255) NOT NULL,
  CONSTRAINT pk_priority PRIMARY KEY  (id)
);

CREATE TABLE task (
  id                 BIGINT       NOT NULL,
  name               VARCHAR(255) NOT NULL,
  employee_id        BIGINT       NOT NULL,
  priority_id        BIGINT       NOT NULL,
  status_id          BIGINT       NOT NULL,
  project_id         BIGINT       NOT NULL,
  CONSTRAINT pk_task PRIMARY KEY  (id)
);

CREATE SEQUENCE status_seq   START WITH 5000;
CREATE SEQUENCE priority_seq START WITH 6000;

   ALTER TABLE task
ADD CONSTRAINT fk_task_employee
   FOREIGN KEY (employee_id)
    REFERENCES employee(id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

   ALTER TABLE task
ADD CONSTRAINT fk_task_priority
   FOREIGN KEY (priority_id)
    REFERENCES priority(id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

   ALTER TABLE task
ADD CONSTRAINT fk_task_status
   FOREIGN KEY (status_id)
    REFERENCES status(id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

   ALTER TABLE task
ADD CONSTRAINT fk_task_project
   FOREIGN KEY (project_id)
    REFERENCES project(id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS project;

DROP TABLE IF EXISTS pos;
DROP TABLE IF EXISTS employee;

SET REFERENTIAL_INTEGRITY TRUE;

DROP SEQUENCE IF EXISTS customer_seq;
DROP SEQUENCE IF EXISTS project_seq;

DROP SEQUENCE IF EXISTS position_seq;
DROP SEQUENCE IF EXISTS employee_seq;