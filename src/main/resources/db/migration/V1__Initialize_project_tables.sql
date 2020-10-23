create table administrator (
    id bigint not null auto_increment,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table applicant (
    id bigint not null auto_increment,
    birthday date not null,
    email varchar(255) not null,
    gender varchar(255) not null,
    name varchar(255) not null,
    phone_number varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table application_form_answers (
    application_form_id bigint not null,
    contents longtext,
    recruitment_item_id bigint not null
) engine=InnoDB default charset=utf8mb4;

create table application_form (
    id bigint not null auto_increment,
    applicant_id bigint not null,
    created_date_time datetime(6) not null,
    modified_date_time datetime(6) not null,
    recruitment_id bigint not null,
    reference_url varchar(255),
    submitted bit not null,
    submitted_date_time datetime(6),
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table cheater (
    id bigint not null auto_increment,
    applicant_id bigint not null,
    created_date_time datetime(6) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table evaluation (
    id bigint not null auto_increment,
    before_evaluation_id bigint not null,
    description varchar(255) not null,
    recruitment_id bigint not null,
    title varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table evaluation_target_answers (
    evaluation_target_id bigint not null,
    evaluation_item_id bigint not null,
    score integer not null
) engine=InnoDB default charset=utf8mb4;

create table evaluation_item (
    id bigint not null auto_increment,
    description varchar(255) not null,
    evaluation_id bigint not null,
    maximum_score integer not null,
    position integer not null,
    title varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table evaluation_target (
    id bigint not null auto_increment,
    administrator_id bigint,
    applicant_id bigint not null,
    evaluation_id bigint not null,
    evaluation_status varchar(255) not null,
    note varchar(255),
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table recruitment (
    id bigint not null auto_increment,
    hidden bit not null,
    end_date_time datetime(6) not null,
    start_date_time datetime(6) not null,
    recruitable bit not null,
    title varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table recruitment_item (
    id bigint not null auto_increment,
    description longtext not null,
    maximum_length integer not null,
    position integer not null,
    recruitment_id bigint not null,
    title varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table administrator
    add constraint UK_qv926x9u07ru5erc4vn3lxhcg unique (name);

alter table applicant
    add constraint UK_6iduje2h6ggdlnmevw2mvolfx unique (email);

alter table application_form_answers
    add constraint FKn8fkfjkg0n1ossav3w1ulek2x
    foreign key (application_form_id)
    references application_form (id);

alter table evaluation_target_answers
    add constraint FKb3q3cjiofqwaba0682l87axlu
    foreign key (evaluation_target_id)
    references evaluation_target (id);
