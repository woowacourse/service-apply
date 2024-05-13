alter table member
    add github_username varchar(255) not null after gender;

alter table assignment
    drop column github_username;