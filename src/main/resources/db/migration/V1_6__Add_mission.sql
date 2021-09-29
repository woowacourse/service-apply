create table mission
(
    id              bigint       not null auto_increment,
    title           varchar(255) not null,
    description     varchar(255) not null,
    evaluation_id   bigint       not null,
    submittable     boolean      not null,
    end_date_time   datetime(6) not null,
    start_date_time datetime(6) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table mission
    add constraint uk_mission unique (evaluation_id);