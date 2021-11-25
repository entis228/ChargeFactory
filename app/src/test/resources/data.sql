insert into user_statuses (id, description)
values (0, 'ACTIVE');
insert into user_statuses (id, description)
values (1, 'SUSPENDED');
insert into authorities (id, value)
values (0, 'ROLE_USER');
insert into authorities (id, value)
values (1, 'ROLE_ADMIN');
insert into authorities (id, value)
values (2, 'ROLE_OWNER');
insert into station_states (id, description)
values (0, 'WAITING_FOR_PLUG');
insert into station_states (id, description)
values (1, 'WAITING_FOR_AUTH');
insert into station_states (id, description)
values (2, 'CHARGING');
insert into station_states (id, description)
values (3, 'END_OF_CHARGE');