delete from message_table;

insert into message_table(id, tag, text, user_id) values
(1, 'my-tag','first', 1),
(2, 'my-tag','second', 1),
(3, 'some','third', 1),
(4, 'body','fourth', 1);

 alter sequence hibernate_sequence restart with 10;