create table if not exists "user_category_point"
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar not null    constraint user_category_point_id_fk REFERENCES "user"(id),
    category_id varchar not null    constraint user_category_point_category_id_fk REFERENCES "category"(id),

    creation_datetime TIMESTAMP  WITH TIME ZONE  DEFAULT current_timestamp NOT NULL,
                                     view_number Bigint default 0 not null,
                                     posted_number Bigint default 0 not null
                                     );

create index if not exists user_category_point_user_id_index on "user_category_point" (user_id);
create index if not exists user_category_point_category_id_index on "user_category_point" (category_id);


ALTER TABLE "user_category_point"
    ADD CONSTRAINT user_post_unq UNIQUE(user_id, category_id);