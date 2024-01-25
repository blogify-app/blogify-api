create table if not exists post_reaction
(
    post_id varchar  not null    constraint post_reaction__post_id_fk REFERENCES post(id)
    ) INHERITS (reaction);
create index if not exists post_reaction_post_id_index on "post_reaction" (post_id);


