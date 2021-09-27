create table user (select * from applicant);

alter table user
    add primary key (id);

alter table user
    add constraint uk_user unique (email);

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

truncate applicant;

alter table applicant
    add column recruitment_id bigint not null after user_id;

alter table applicant
    add constraint fk_user
    foreign key (user_id)
    references user (id);
