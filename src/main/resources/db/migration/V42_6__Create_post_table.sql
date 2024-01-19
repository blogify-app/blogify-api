create table if not exists post
(
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    creation_datetime TIMESTAMP,
    last_update_datetime TIMESTAMP
);
create index if not exists post_id_index on post(id);

-- TODO : create index for user_id
