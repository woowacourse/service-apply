alter table mission
    add deleted boolean not null after id;

alter table mission
    drop key uk_mission;
