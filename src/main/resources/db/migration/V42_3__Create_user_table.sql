CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "user" (
   id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
   firstname VARCHAR(100)g
   lastname VARCHAR(100),
   mail VARCHAR(250) NOT NULL,
   birthdate DATE,

   role VARCHAR(100) NOT NULL,
    sex VARCHAR(100),
   creation_datetime TIMESTAMP DEFAULT current_timestamp NOT NULL,
   last_update_datetime TIMESTAMP
);