create table assignment
(
    id               bigint       not null auto_increment,
    user_id          bigint       not null,
    github_username  varchar(255) not null,
    mission_id       bigint       not null,
    note             longtext,
    pull_request_url varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table assignment
    add constraint uk_assignment unique (user_id, mission_id);
