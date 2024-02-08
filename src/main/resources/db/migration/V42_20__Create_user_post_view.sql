create table if not exists "user_post_view"
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar not null    constraint user_post_view_user_id_fk REFERENCES "user"(id),
    post_id varchar not null    constraint user_post_view_post_id_fk REFERENCES "post"(id),

    creation_datetime TIMESTAMP  WITH TIME ZONE  DEFAULT current_timestamp NOT NULL,
        is_upload boolean default false not null
);

create index if not exists user_post_view_user_id_index on "user_post_view" (user_id);
create index if not exists user_post_view_post_id_index on "user_post_view" (post_id);

