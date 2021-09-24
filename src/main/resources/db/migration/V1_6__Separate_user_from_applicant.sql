create table user
(
    id bigint not null auto_increment,
    birthday date not null,
    email varchar(255) not null,
    gender varchar(255) not null,
    name varchar(255) not null,
    phone_number varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table applicant
    drop index UK_6iduje2h6ggdlnmevw2mvolfx;

alter table applicant
    drop column password;

alter table applicant
    drop column birthday;

alter table applicant
    drop column email;

alter table applicant
    drop column gender;

alter table applicant
    drop column name;

alter table applicant
    drop column phone_number;

alter table applicant
    add column user_id bigint not null after id;

alter table applicant
    add column recruitment_id bigint not null after user_id;

alter table applicant
    add constraint fk_user
    foreign key (user_id)
    references user (id);

alter table user
    add constraint uk_user unique (email);
