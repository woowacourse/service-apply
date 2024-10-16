create table member_information
(
    id              bigint       not null auto_increment,
    birthday        date         not null,
    email           varchar(255) not null,
    github_username varchar(39)  not null,
    name            varchar(30)  not null,
    phone_number    varchar(13)  not null,
    member_id       bigint       not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table member_information
    add constraint uk_member_information unique (email);

alter table member
    drop birthday,
    drop email,
    drop github_username,
    drop name,
    drop phone_number;
