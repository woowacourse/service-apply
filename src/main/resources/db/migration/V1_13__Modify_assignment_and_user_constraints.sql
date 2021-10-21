alter table assignment
    modify github_username varchar(39) not null,
    modify note varchar(1000);

alter table user
    modify gender varchar(6) not null,
    modify name varchar(30) not null,
    modify phone_number varchar(13) not null;
