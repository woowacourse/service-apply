create table term
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table recruitment
    add term_id bigint default 0 after title;

alter table application_form
    add constraint uk_application_form unique (recruitment_id, applicant_id);
