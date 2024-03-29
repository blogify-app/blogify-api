CREATE TABLE IF NOT EXISTS category (
    id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
   name VARCHAR(50),
   creation_datetime TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp
);

create index if not exists category_name_index on "category" (name);