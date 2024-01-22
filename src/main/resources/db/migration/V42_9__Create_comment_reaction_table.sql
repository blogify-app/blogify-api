CREATE TABLE IF NOT EXISTS "comment_reaction"
(
    comment_id VARCHAR NOT NULL CONSTRAINT comment_reaction__comment_id_fk REFERENCES comment(id)
)INHERITS (reaction);