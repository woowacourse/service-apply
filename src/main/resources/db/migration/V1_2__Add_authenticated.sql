alter table applicant
    add authenticated boolean not null;

alter table applicant
    add authenticate_code varchar(255) not null;