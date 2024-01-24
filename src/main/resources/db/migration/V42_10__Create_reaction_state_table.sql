CREATE TABLE IF NOT EXISTS "reaction_state"
(
    likes DECIMAL,
    dislikes DECIMAL,
    comment_id VARCHAR NOT NULL CONSTRAINT comment_reaction__comment_id_fk REFERENCES comment(id)
)INHERITS (reaction);