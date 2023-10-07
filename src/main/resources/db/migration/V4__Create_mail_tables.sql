create table mail_message
(
    id         bigint       not null auto_increment,
    subject    varchar(255) not null,
    body       longtext     not null,
    sender     varchar(255) not null,
    recipients longtext     not null,
    creator_id bigint       not null,
    created_date_time  datetime(6)  not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

create table mail_reservation
(
    id              bigint       not null auto_increment,
    status          varchar(8)   not null,
    mail_message_id bigint  not null,
    reservation_time  datetime(6)  not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table mail_reservation
    add constraint fk_mail_reservation_to_mail_message
    foreign key (mail_message_id)
    references mail_message (id);

rename table mail_history to mail_history_old;

create table mail_history
(
    id         bigint       not null auto_increment,
    recipients longtext     not null,
    sent_time  datetime(6)  not null,
    success    bit(1)       not null,
    mail_message_id bigint  not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table mail_history
    add constraint fk_mail_history_to_mail_message
    foreign key (mail_message_id)
    references mail_message (id);
