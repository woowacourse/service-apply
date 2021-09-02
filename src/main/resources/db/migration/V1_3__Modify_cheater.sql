alter table cheater
    change applicant_id email varchar(255) not null;

alter table cheater
    add description longtext not null after email;
