alter table cheater
    add description longtext not null after created_date_time;

alter table cheater
    add email varchar(255) not null after description;

alter table cheater
    modify column applicant_id bigint after email;
