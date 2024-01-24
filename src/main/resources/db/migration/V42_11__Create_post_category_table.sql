CREATE TABLE IF NOT EXISTS post_category (
    id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
    post_id VARCHAR,
    category_id VARCHAR,
    FOREIGN KEY(post_id) REFERENCES post(id),
    FOREIGN KEY(category_id) REFERENCES category(id)
);