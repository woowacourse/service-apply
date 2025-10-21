alter table member
    add status varchar(20) not null default 'ACTIVE' after password,
    add index ix_member_status_id (status, id);

alter table member_information
    drop primary key,
    drop column id,
    add primary key (member_id);

alter table member_information
    add constraint fk_member_information_member_id_ref_member_id
        foreign key (member_id)
            references member (id);
