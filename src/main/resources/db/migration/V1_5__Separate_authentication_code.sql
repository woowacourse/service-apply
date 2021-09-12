create table authentication_code
(
    id                bigint       not null auto_increment,
    authenticated     boolean      not null,
    code              char(8)      not null,
    created_date_time datetime(6)  not null,
    email             varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;
