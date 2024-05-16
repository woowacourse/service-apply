alter table member
    drop gender,
    add constraint uk_member unique (email),
    drop index UK_6iduje2h6ggdlnmevw2mvolfx;
