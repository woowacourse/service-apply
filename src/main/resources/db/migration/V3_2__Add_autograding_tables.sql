create table auto_grading_item
(
    id                  bigint       not null auto_increment,
    mission_id          bigint       not null,
    evaluation_item_id  bigint       not null,
    grading_name        varchar(255)       not null,
    programming_language varchar(255) not null,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

create table test_execution_history
(
    id            bigint       not null auto_increment,
    user_id       bigint       not null,
    mission_id    bigint       not null,
    request_key   bigint       not null unique,
    commit_id     char(40)     not null,
    test_type     varchar(255) not null,
    correct_count int,
    total_count   int,
    message       varchar(255),
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;