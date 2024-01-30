create table if not exists picture
(
    id varchar primary key default uuid_generate_v4(),
    creation_datetime TIMESTAMP  WITH TIME ZONE  DEFAULT current_timestamp NOT NULL,
    bucket_key varchar not null,
    disc_type VARCHAR(4) NOT NULL
);
