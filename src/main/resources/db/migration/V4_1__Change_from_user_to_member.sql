alter table user rename member;

alter table application_form
    change user_id member_id bigint not null after created_date_time;

alter table assignment
    change user_id member_id bigint not null after github_username;

alter table evaluation_target
    change user_id member_id bigint not null after evaluation_status;

alter table assignment
    rename index idx_user_id to idx_member_id;
