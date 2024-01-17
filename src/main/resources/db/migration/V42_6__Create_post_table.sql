create table if not exists post
(
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    creation_datetime TIMESTAMP,
    last_update_datetime TIMESTAMP
);