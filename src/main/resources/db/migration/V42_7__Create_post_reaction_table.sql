create type "reaction_type" as enum ('LIKE', 'DISLIKE');

create table if not exists post_reaction
(
    id VARCHAR(255) PRIMARY KEY,
    id_post VARCHAR(255) REFERENCES post(id),
    type reaction_type
    );
