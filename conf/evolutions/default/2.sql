# --- Sample dataset
# --- !Ups

INSERT INTO customer(id, name) VALUES (0, 'Apple Inc.');
INSERT INTO customer(id, name) VALUES (1, 'Thinking Machines');
INSERT INTO customer(id, name) VALUES (2, 'RCA');
INSERT INTO customer(id, name) VALUES (3, 'Netronics');
INSERT INTO customer(id, name) VALUES (4, 'Tandy Corporation');

INSERT INTO project(id, name, customer_id) VALUES (5, 'RestApi',       0);
INSERT INTO project(id, name, customer_id) VALUES (6, 'AnormExample',  1);
INSERT INTO project(id, name, customer_id) VALUES (7, 'TeamTask',      2);
INSERT INTO project(id, name, customer_id) VALUES (8, 'Forms',         3);
INSERT INTO project(id, name, customer_id) VALUES (9, 'AkkaActors',    4);

INSERT INTO pos(id, name) VALUES (10, 'Junior');
INSERT INTO pos(id, name) VALUES (11, 'Middle');
INSERT INTO pos(id, name) VALUES (12, 'Senior');
INSERT INTO pos(id, name) VALUES (13, 'Lead');
INSERT INTO pos(id, name) VALUES (14, 'Architect');

INSERT INTO status(id, name) VALUES (15, 'Opened');
INSERT INTO status(id, name) VALUES (16, 'In progress');
INSERT INTO status(id, name) VALUES (17, 'Review');
INSERT INTO status(id, name) VALUES (18, 'Resolved');
INSERT INTO status(id, name) VALUES (19, 'Postponed');

INSERT INTO priority(id, name) VALUES (20, 'Low');
INSERT INTO priority(id, name) VALUES (21, 'Medium');
INSERT INTO priority(id, name) VALUES (22, 'High');
INSERT INTO priority(id, name) VALUES (23, 'Immediate');

INSERT INTO employee(id, name, position_id) VALUES (24, 'dmedser',     10);
INSERT INTO employee(id, name, position_id) VALUES (25, 'l0gitech',    11);
INSERT INTO employee(id, name, position_id) VALUES (26, 'Git master',  12);
INSERT INTO employee(id, name, position_id) VALUES (27, 'pet',         13);
INSERT INTO employee(id, name, position_id) VALUES (28, '_admin_',     14);

INSERT INTO task(id, name, employee_id, priority_id, status_id, project_id) VALUES (29, 'Create git repository',     24, 20, 16, 5);
INSERT INTO task(id, name, employee_id, priority_id, status_id, project_id) VALUES (30, 'Make everything wonderful', 25, 21, 17, 6);
INSERT INTO task(id, name, employee_id, priority_id, status_id, project_id) VALUES (31, 'Do something',              26, 22, 18, 7);
INSERT INTO task(id, name, employee_id, priority_id, status_id, project_id) VALUES (32, 'Import changes',            27, 23, 16, 8);
INSERT INTO task(id, name, employee_id, priority_id, status_id, project_id) VALUES (33, 'Enable Auto-Import',        28, 22, 18, 9);

# --- !Downs

DELETE FROM customer;
DELETE FROM project;
DELETE FROM pos;
DELETE FROM employee;
DELETE FROM status;
DELETE FROM priority;
DELETE FROM task;