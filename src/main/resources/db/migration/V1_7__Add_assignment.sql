create table assignment
(
    id           bigint       not null auto_increment,
    applicant_id bigint       not null,
    git_account    varchar(255) not null,
    impression   longtext,
    mission_id   bigint       not null,
    url          varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table assignment
    add constraint uk_assignment unique (mission_id, applicant_id);
