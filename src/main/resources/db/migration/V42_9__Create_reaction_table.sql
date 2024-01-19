CREATE TYPE reaction_type AS ENUM ('LIKE', 'DISLIKE'); -- TODO 1 - remove

create table if not exists reaction
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar REFERENCES "user"(id), -- TODO 5: same as TODO 4
    reaction_type reaction_type NOT NULL -- TODO 2 - change to VARCHAR
);