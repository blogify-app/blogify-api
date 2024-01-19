create table if not exists comment_reaction
(
    comment_id varchar  not null    constraint comment_reaction__comment_id_fk REFERENCES comment(id) -- TODO 4 - add fk constraint and not null option for id of other table, do it for all fk id attribute
)INHERITS (reaction); -- TODO 3 - add inheritance to reaction