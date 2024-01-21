create type "reaction_type_enum" as enum ('LIKE', 'DISLIKE');

create table if not exists "reaction"
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar not null    constraint reaction__user_id_fk REFERENCES "user"(id),
    type varchar NOT NULL
);