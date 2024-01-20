create type "reaction_type_enum" as enum ('LIKE', 'DISLIKE');

create table if not exists post_reaction
(
    id VARCHAR(255) PRIMARY KEY,
    post_id VARCHAR(255) REFERENCES post(id),
    reaction_type reaction_type_enum
    );
