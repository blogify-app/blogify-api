CREATE TABLE IF NOT EXISTS post_category (
    id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
    post_id VARCHAR constraint post_category_post_id_fk REFERENCES post(id),
    category_id VARCHAR constraint post_category_category_id_fk REFERENCES category(id)
);

create index if not exists post_category_post_id_index on "post_category" (post_id);
create index if not exists post_category_category_id_index on "post_category" (category_id);
