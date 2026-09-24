create table client
(
    id   bigserial primary key,
    name varchar(255) not null
);

create table manager
(
    no    bigserial primary key,
    label varchar(255) not null
);
