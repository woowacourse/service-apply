create table mail_history2
(
    id         bigint       not null auto_increment,
    recipients longtext     not null,
    sent_time  datetime(6)  not null,
    success    bit(1)       not null,
    mail_message_id bigint  not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table mail_history2
    add constraint fk_mail_history_to_mail_message
    foreign key (mail_message_id)
    references mail_message (id);