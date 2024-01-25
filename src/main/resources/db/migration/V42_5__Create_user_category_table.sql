CREATE TABLE IF NOT EXISTS user_category (
    id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
    user_id VARCHAR constraint user_category_user_id_fk REFERENCES "user"(id),
    category_id VARCHAR constraint user_category_category_fk REFERENCES category(id)
);
create index if not exists user_category_user_id_index on "user_category" (user_id);
create index if not exists user_category_category_id_index on "user_category" (category_id);

