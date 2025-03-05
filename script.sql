create database csti;

create table solicitud
(
    id              bigserial
        primary key,
    codigo          varchar(255),
    fecha_envio     date,
    marca           varchar(255),
    nombre_contacto varchar(255),
    numero_contacto varchar(255),
    tipo_solicitud  varchar(255)
);

alter table solicitud
    owner to postgres;

create table contacto
(
    id              bigserial
        primary key,
    nombre          varchar(255),
    numero_contacto varchar(255),
    solicitud_id    bigint not null
        constraint fkmn5lwy15fltsb8510298rx8xi
            references solicitud
);

alter table contacto
    owner to postgres;