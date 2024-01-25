create table if not exists "reaction"
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar not null    constraint reaction__user_id_fk REFERENCES "user"(id),
    creation_datetime TIMESTAMP  WITH TIME ZONE  DEFAULT current_timestamp NOT NULL,
    type varchar NOT NULL
);

create index if not exists reaction_user_id_index on "reaction" (user_id);
create index if not exists reaction_type_index on "reaction" (type);
