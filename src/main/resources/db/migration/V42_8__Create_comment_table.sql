create table if not exists comment
(
    id UUID PRIMARY KEY,
    content TEXT,
    creation_date_time TIMESTAMP
);