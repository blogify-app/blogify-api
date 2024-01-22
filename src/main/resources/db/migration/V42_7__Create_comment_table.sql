CREATE TABLE IF NOT EXISTS "comment"
(
    id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    content TEXT,
    creation_datetime TIMESTAMP DEFAULT NOW(),
    user_id VARCHAR NOT NULL CONSTRAINT comment__user_id_fk REFERENCES "user"(id),
    reply_to_id VARCHAR,
    -- post_id VARCHAR NOT NULL CONSTRAINT comment__post_id_fk REFERENCES post(id),
    status VARCHAR NOT NULL
);
-- CREATE INDEX if not exists idx_comment_post_id ON comment (post_id);