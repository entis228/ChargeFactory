alter table authorities add column if not exists value varchar(255) not null;
create table user_statuses
(
    id          integer  not null primary key,
    description varchar(255) not null
);
create table station_states
(
    id integer  not null primary key,
    description varchar(255) not null
);