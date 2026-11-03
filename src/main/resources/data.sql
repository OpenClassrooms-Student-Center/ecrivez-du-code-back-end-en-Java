-- Jeu de données de démonstration, rechargé à chaque démarrage (base H2 en mémoire).
-- Les dates sont calculées à partir de la date du jour.

INSERT INTO employee (first_name, last_name, contract_type, hire_date, days_taken)
VALUES ('Amina', 'Diallo', 'CDI', DATEADD('MONTH', -2, DATEADD('YEAR', -4, CURRENT_DATE)), 8);
INSERT INTO employee (first_name, last_name, contract_type, hire_date, days_taken)
VALUES ('Karim', 'Benali', 'CDI', DATEADD('MONTH', -5, DATEADD('YEAR', -6, CURRENT_DATE)), 12);
INSERT INTO employee (first_name, last_name, contract_type, hire_date, days_taken)
VALUES ('Mei', 'Chen', 'Alternance', DATEADD('MONTH', -1, DATEADD('YEAR', -1, CURRENT_DATE)), 6);
INSERT INTO employee (first_name, last_name, contract_type, hire_date, days_taken)
VALUES ('Leïla', 'Haddad', 'CDD', DATEADD('DAY', -5, DATEADD('MONTH', -7, CURRENT_DATE)), 10);
INSERT INTO employee (first_name, last_name, contract_type, hire_date, days_taken)
VALUES ('Sofia', 'Rossi', 'CDD', DATEADD('DAY', -2, DATEADD('MONTH', -11, CURRENT_DATE)), 4);

INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (1, DATEADD('DAY', 14, CURRENT_DATE), DATEADD('DAY', 18, CURRENT_DATE), 5, 'Pending');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (3, DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -38, CURRENT_DATE), 3, 'Approved');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (2, DATEADD('DAY', 30, CURRENT_DATE), DATEADD('DAY', 31, CURRENT_DATE), 2, 'Pending');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (5, DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -17, CURRENT_DATE), 4, 'Approved');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (3, DATEADD('DAY', 21, CURRENT_DATE), DATEADD('DAY', 25, CURRENT_DATE), 5, 'Refused');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (2, DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -56, CURRENT_DATE), 5, 'Approved');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (4, DATEADD('DAY', 35, CURRENT_DATE), DATEADD('DAY', 39, CURRENT_DATE), 5, 'Pending');
INSERT INTO leave_request (employee_id, start_date, end_date, working_days, status)
VALUES (5, DATEADD('DAY', 45, CURRENT_DATE), DATEADD('DAY', 46, CURRENT_DATE), 2, 'Pending');
