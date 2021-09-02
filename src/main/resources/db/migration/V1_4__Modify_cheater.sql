alter table cheater
    modify column applicant_id bigint;

alter table cheater
    add email varchar(255) not null after applicant_id;

alter table cheater
    add description longtext not null after email;
