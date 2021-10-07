alter table applicant rename user;

alter table application_form
    change applicant_id user_id bigint not null after id;

alter table evaluation_target
    change applicant_id user_id bigint not null after administrator_id;

