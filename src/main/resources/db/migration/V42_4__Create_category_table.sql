CREATE TABLE IF NOT EXISTS category (
    id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
   name VARCHAR(50),
   creation_datetime TIMESTAMP DEFAULT current_timestamp
);