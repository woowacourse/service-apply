create table agreement
(
    id      bigint        not null auto_increment,
    content varchar(5000) not null,
    version integer       not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;
