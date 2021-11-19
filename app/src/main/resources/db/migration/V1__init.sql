create table user_statuses
(
    id          int  not null primary key,
    description text not null
);

insert into user_statuses (id, description)
values (0, 'ACTIVE');
insert into user_statuses (id, description)
values (1, 'SUSPENDED');

create table users
(
    id uuid primary key,
    email text not null,
    name text not null,
    surname text,
    phone text,
    status int not null references user_statuses (id),
    password text not null,
    balance numeric not null
);

create unique index users_email_uindex on users (email);

create table authorities
(
    id    int primary key,
    value text not null
);

create unique index authorities_value_uindex on authorities (value);

create table user_authorities
(
    user_id      uuid not null,
    authority_id int    not null,
    primary key (user_id, authority_id),
    constraint user_authorities_users_fk foreign key (user_id)
        references users (id) on delete cascade,
    constraint user_authorities_authorities_fk foreign key (authority_id)
        references authorities (id) on delete cascade
);

insert into authorities (id, value)
values (0, 'ROLE_USER');
insert into authorities (id, value)
values (1, 'ROLE_ADMIN');
insert into authorities (id, value)
values (2, 'ROLE_OWNER');

create table refresh_tokens
(
    value     uuid        not null primary key,
    user_id   uuid      not null,
    issued_at timestamptz not null,
    expire_at timestamptz not null,
    next      uuid,
    constraint refresh_tokens_user_fk foreign key (user_id)
        references users (id) on delete cascade,
    constraint refresh_tokens_next_fk foreign key (next)
        references refresh_tokens (value) on delete cascade
);

create table station_states
(
    id int  not null primary key,
    description text not null
);

insert into station_states (id, description)
values (0, 'WAITING_FOR_PLUG');
insert into station_states (id, description)
values (1, 'WAITING_FOR_AUTH');
insert into station_states (id, description)
values (2, 'CHARGING');
insert into station_states (id, description)
values (3, 'END_OF_CHARGE');

create table stations
(
    id uuid primary key,
    name text not null,
    state int not null references station_states (id)
);

create table charges
(
    id uuid primary key,
    endtime timestamptz not null default now(),
    user_id uuid not null references users (id),
    station_id uuid not null references stations (id),
    consumed float not null,
    withdraw numeric not null
);