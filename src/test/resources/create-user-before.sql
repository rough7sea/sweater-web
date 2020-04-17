delete from user_role;
delete from usr;

insert into usr(id, active, password, user_name) VALUES
(1, true, '$2a$08$qlz/HSTxPhq8.B6d7B2RHuhqu17X.kM/QwdPSOexDcwYMJhtkDbBS', 'Danila'),
(2, true, '$2a$08$qlz/HSTxPhq8.B6d7B2RHuhqu17X.kM/QwdPSOexDcwYMJhtkDbBS', 'mike');

insert into user_role(user_id, roles) values
(1, 'USER'),(1, 'ADMIN'),
(2, 'USER');