alter table mission
    add submission_method varchar(255) not null default 'PUBLIC_PULL_REQUEST' after start_date_time;
