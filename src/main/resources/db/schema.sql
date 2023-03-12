drop table if exists likes;
drop table if exists users_excursion;
drop table if exists comment;
drop table if exists users;
drop table if exists excursion;

create table users
(
    id            int generated by default as identity primary key,
    name          varchar(100)            not null,
    email         varchar(100) unique     not null,
    phone_number  varchar(10) unique      not null,
    password      varchar(256)            not null,
    registered_at timestamp default now() not null,
    role          varchar(100)            not null
);

create table excursion
(
    id          int generated by default as identity primary key,
    name        varchar(100) not null,
    date        date,
    description varchar(300),
    price       int check ( price >= 0 )
);

create table users_excursion
(
    users_id     int references users (id),
    excursion_id int references excursion (id),
    primary key (users_id, excursion_id)
);

create table comment
(
    id           int generated by default as identity primary key,
    message      varchar(300)            not null,
    created_at   timestamp default now() not null,
    users_id     int                     references users (id) on delete set null,
    excursion_id int                     references excursion on delete set null
);

create table likes
(
    users_id     int references users (id),
    excursion_id int references excursion (id),
    created_at   timestamp default now() not null,
    primary key (users_id, excursion_id)
);

