CREATE TABLE IF NOT EXISTS user_category (
    id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
    user_id VARCHAR,
    category_id VARCHAR,
    FOREIGN KEY(user_id) REFERENCES "user"(id),
    FOREIGN KEY(category_id) REFERENCES category(id)
);
