create table email_history
(
    id         bigint not null auto_increment,
    subject    varchar(255) not null,
    body       varchar(255) not null,
    sender     varchar(255) not null,
    recipients varchar(255) not null,
    sent_time  datetime(6) not null,
    primary key (id)
) engine = InnoDB
   default charset = utf8mb4;
