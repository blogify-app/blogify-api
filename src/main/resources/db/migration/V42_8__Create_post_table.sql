create table if not exists post
(
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    thumbnail_url VARCHAR,
    description TEXT,
    status VARCHAR,
    user_id VARCHAR not null constraint post_user_id_fk REFERENCES "user"(id),
    creation_datetime TIMESTAMP  WITH TIME ZONE  DEFAULT current_timestamp NOT NULL,
    last_update_datetime TIMESTAMP  WITH TIME ZONE DEFAULT current_timestamp
);
create index if not exists post_user_id_index on "post" (user_id);

