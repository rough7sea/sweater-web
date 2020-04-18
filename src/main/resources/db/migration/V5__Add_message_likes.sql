create table message_likes(
    user_id bigint not  null references usr,
    message_id bigint not  null references message_table,
    primary key (user_id, message_id)
)