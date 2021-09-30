# Data users
INSERT INTO payMyBuddy_test.user (email, first_name, last_name, password)
VALUES ('jamesharden@gmail.com', 'james', 'harden', 'password'),
       ('lebronjames@gmail.com', 'lebron', 'james', 'password'),
       ('mikejames@gmail.com', 'mike', 'james', 'password'),
       ('rayallen@gmail.com', 'ray', 'allen', 'password'),
       ('juniorsmith@gmail.com', 'junior', 'smith', 'password');

# Data connection
INSERT INTO payMyBuddy_test.connection (id_user, id_user_connected)
VALUES (1, 2),
       (1, 5),
       (1, 3),
       (2, 4),
       (4, 3),
       (4, 1),
       (4, 2),
       (5, 1);

# Data transfer
INSERT INTO payMyBuddy_test.transfer (amount, date, payment_method, transfert_type, user)
VALUES (12.00, '2021-04-15 12:23:03', 'CREDIT_CARD', 'CREDIT', 1),
       (36.30, '2021-12-23 14:30:03', 'BANK_TRANSFER', 'DEBIT', 4),
       (53.80, '2020-06-03 04:43:43', 'CREDIT_CARD', 'CREDIT', 3);

# Data transaction
INSERT INTO payMyBuddy_test.transaction (amount, cost_100, date, description, user_creditor, user_debtor)
VALUES (50.00, 5.00, '2021-01-13 14:30:03', 'trip money', 2, 1),
       (100.70, 5.00, '2020-03-12 14:30:03', 'cinema', 1, 5),
       (234.00, 5.00, '2021-09-19 14:30:03', 'restaurant', 1, 4),
       (234.30, 5.00, '2021-12-13 14:30:03', 'movie tickets', 3, 4);

