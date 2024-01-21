create table if not exists "comment_reaction"
(
    comment_id varchar  not null    constraint comment_reaction__comment_id_fk REFERENCES comment(id)
)INHERITS (reaction);