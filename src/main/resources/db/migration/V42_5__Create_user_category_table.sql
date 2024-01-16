CREATE TABLE IF NOT EXISTS user_category (
   user_id VARCHAR,
   category_id INT,
   PRIMARY KEY(user_id, category_id),
   FOREIGN KEY(user_id) REFERENCES "user"(id),
   FOREIGN KEY(category_id) REFERENCES category(id)
);
