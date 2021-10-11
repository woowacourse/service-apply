create table mail_history
(
    id         bigint       not null auto_increment,
    subject    varchar(255) not null,
    body       longtext     not null,
    sender     varchar(255) not null,
    recipients longtext     not null,
    sent_time  datetime(0)  not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;
