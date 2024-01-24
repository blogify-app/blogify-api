do
    $$
        begin
            if not exists(select from pg_type where typname = 'status') then
            create type "status" as enum ('ENABLED','DISABLED');
            end if;
        end
    $$;

CREATE TABLE IF NOT EXISTS "comment"
(
    id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    content TEXT,
    creation_datetime TIMESTAMP DEFAULT NOW(),
    user_id VARCHAR NOT NULL CONSTRAINT comment__user_id_fk REFERENCES "user"(id),
    reply_to_id VARCHAR,
    post_id VARCHAR NOT NULL,
    status status NOT NULL
);
CREATE INDEX if not exists idx_comment_user_id ON comment (user_id);