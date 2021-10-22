alter table applicant rename user;

alter table application_form
    change applicant_id user_id bigint not null after submitted_date_time;

alter table cheater
    change applicant_id user_id bigint after created_date_time;

alter table evaluation_target
    change applicant_id user_id bigint not null after note;

create table assignment
(
    id               bigint       not null auto_increment,
    user_id          bigint       not null,
    github_username  varchar(39)  not null,
    mission_id       bigint       not null,
    note             varchar(1000),
    pull_request_url varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

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

create table mail_history
(
    id         bigint       not null auto_increment,
    subject    varchar(255) not null,
    body       longtext     not null,
    sender     varchar(255) not null,
    recipients longtext     not null,
    sent_time  datetime(6)  not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

create table mission
(
    id              bigint       not null auto_increment,
    deleted         boolean      not null,
    description     varchar(255) not null,
    evaluation_id   bigint       not null,
    hidden          boolean      not null,
    end_date_time   datetime(6)  not null,
    start_date_time datetime(6)  not null,
    submittable     boolean      not null,
    title           varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

create table term
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table application_form
    add constraint uk_application_form unique (recruitment_id, user_id);

alter table assignment
    add constraint uk_assignment unique (user_id, mission_id),
    add index idx_user_id (user_id);

alter table cheater
    add description longtext     not null after created_date_time,
    add email       varchar(255) not null after description;

alter table recruitment
    add deleted boolean not null after id,
    add term_id bigint default 0 after recruitable;

alter table user
    modify gender varchar(6) not null,
    modify name varchar(30) not null,
    modify phone_number varchar(13) not null;
