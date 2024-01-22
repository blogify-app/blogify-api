create table if not exists post
(
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    creation_datetime TIMESTAMP DEFAULT current_timestamp,
    last_update_datetime TIMESTAMP
);
-- TODO : create index for user_id
