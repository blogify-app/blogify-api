CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create table if not exists comment
(
    id varchar primary key default uuid_generate_v4(),
    content TEXT,
    creation_date_time TIMESTAMP
);