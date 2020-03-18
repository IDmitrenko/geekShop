create database onlineshop with owner postgres;

create table if not exists image (
                                     id UUID DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT image_pk PRIMARY KEY ,
                                     name VARCHAR(255) NOT NULL
);

alter table image owner to postgres;

create table if not exists product
(
    id uuid default uuid_generate_v4() not null
        constraint product_pk
            primary key,
    title varchar(255) not null,
    description varchar(255),
    price double precision not null,
    added timestamp default now() not null,
    available boolean default true not null,
    image uuid
        constraint fk_product_image
            references image
            on update cascade on delete cascade,
    category integer not null
);

alter table product owner to postgres;

create unique index product_id_uindex
    on product (id);

create unique index product_title_uindex
    on product (title);

create unique index product_image_uindex
    on product (image);

create unique index image_id_uindex
    on image (id);

create unique index image_name_uindex
    on image (name);

create or replace function uuid_nil()
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_nil() owner to postgres;

create or replace function uuid_ns_dns()
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_ns_dns() owner to postgres;

create or replace function uuid_ns_url()
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_ns_url() owner to postgres;

create or replace function uuid_ns_oid()
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_ns_oid() owner to postgres;

create or replace function uuid_ns_x500()
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_ns_x500() owner to postgres;

create or replace function uuid_generate_v1()
    strict
    language c
as -- missing source code
;

alter function uuid_generate_v1() owner to postgres;

create or replace function uuid_generate_v1mc()
    strict
    language c
as -- missing source code
;

alter function uuid_generate_v1mc() owner to postgres;

create or replace function uuid_generate_v3(namespace uuid, name text)
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_generate_v3(uuid, text) owner to postgres;

create or replace function uuid_generate_v4()
    strict
    language c
as -- missing source code
;

alter function uuid_generate_v4() owner to postgres;

create or replace function uuid_generate_v5(namespace uuid, name text)
    immutable
    strict
    language c
as -- missing source code
;

alter function uuid_generate_v5(uuid, text) owner to postgres;