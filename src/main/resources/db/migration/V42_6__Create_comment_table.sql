create table if not exists comment
(
    id varchar primary key default uuid_generate_v4(),
    content TEXT,
    -- post_id varchar REFERENCES post(id)
    creation_datetime TIMESTAMP DEFAULT now()
);
-- CREATE INDEX if not exists idx_comment_post_id ON comment (post_id);