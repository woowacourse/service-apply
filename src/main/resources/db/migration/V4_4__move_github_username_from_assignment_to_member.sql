alter table member
    add github_username varchar(39) not null after email;

alter table assignment
    drop github_username;
