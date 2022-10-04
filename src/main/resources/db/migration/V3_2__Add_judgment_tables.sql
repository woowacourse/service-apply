create table judgment_item
(
    id                   bigint       not null auto_increment,
    evaluation_item_id   bigint       not null,
    programming_language varchar(255) not null,
    test_name            varchar(255) not null,
    mission_id           bigint       not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

create table judgment
(
    id            bigint       not null auto_increment,
    assignment_id bigint       not null,
    type          varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

create table judgment_record
(
    id                  bigint       not null auto_increment,
    commit_hash         char(40)     not null,
    completed_date_time datetime(6),
    message             varchar(255) not null,
    pass_count          integer      not null,
    total_count         integer      not null,
    started_date_time   datetime(6)  not null,
    judgment_id         bigint       not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table judgment_record
    add constraint uk_judgment_record unique (judgment_id, commit_hash);

alter table judgment_record
    add constraint fk_judgment_record_judgment_id_ref_judgment_id
        foreign key (judgment_id)
            references judgment (id);
