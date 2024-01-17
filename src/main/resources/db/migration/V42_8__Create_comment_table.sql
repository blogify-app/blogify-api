create table if not exists comments
(
    id UUID PRIMARY KEY,
    content TEXT,
    creation_date_time TIMESTAMP,
);