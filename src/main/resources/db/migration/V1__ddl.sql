-- liquibase formatted sql
-- see https://docs.liquibase.com/concepts/changelogs/sql-format.html
create schema sentryc_interview;

create table sentryc_interview.producers
(
    id uuid  not null constraint "producersPK" primary key,
    name varchar(255),
    created_at timestamp not null
);

create table sentryc_interview.marketplaces
(
    id  varchar(255) not null constraint "marketplacesPK" primary key,
    description varchar(255)
);

create table sentryc_interview.seller_infos
(
    id uuid not null constraint "seller_infosPK" primary key,
    marketplace_id  varchar(255) constraint "FKr8ekbqgwa3g0uhgbaa1tchf09" references sentryc_interview.marketplaces,
    name                  varchar(2048) not null,
    url                   varchar(2048),
    country               varchar(255),
    external_id           varchar(255),
    constraint "UK12xaxk0c1mwxr3ovycs1qxtbk" unique (marketplace_id, external_id)
);

create table sentryc_interview.sellers
(
    id uuid not null constraint "marketplace_sellersPK" primary key,
    producer_id uuid not null constraint "FK6y70nxr3lhubusfq6ub427ien" references sentryc_interview.producers,
    seller_info_id   uuid constraint "FKp2fkfcqcndx9x9xkhk5va3cq4" references sentryc_interview.seller_infos,
    state varchar(255) default 'REGULAR'::character varying not null,
    constraint "UKsellers" unique (producer_id, seller_info_id)
);

CREATE INDEX IDX_seller_name ON sentryc_interview.seller_infos(name);