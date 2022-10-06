alter table administrator
    modify name varchar(30) not null,
    add username varchar(30) not null after password,
    add constraint uk_administrator unique (username),
    drop index `UK_qv926x9u07ru5erc4vn3lxhcg`;
