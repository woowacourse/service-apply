create table term
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table recruitment
    add term_id bigint after title;
