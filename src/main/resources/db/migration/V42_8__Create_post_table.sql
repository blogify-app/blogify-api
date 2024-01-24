create table if not exists post
(
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    thumbnail_url VARCHAR,
    description TEXT,
    status VARCHAR,
    user_id VARCHAR not null,
    creation_datetime TIMESTAMP DEFAULT current_timestamp,
    last_update_datetime TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES "user"(id)
);
-- TODO : create index for user_id
