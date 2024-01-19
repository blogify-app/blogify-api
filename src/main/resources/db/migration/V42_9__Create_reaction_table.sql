CREATE TYPE reaction_type AS ENUM ('LIKE', 'DISLIKE');

create table if not exists reaction
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar REFERENCES "user"(id),
    reaction_type reaction_type NOT NULL
);