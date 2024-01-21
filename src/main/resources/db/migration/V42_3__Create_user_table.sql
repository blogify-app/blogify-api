CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS "user" (
   id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
   firstname VARCHAR(50),
   lastname VARCHAR(50),
   mail VARCHAR(50) NOT NULL,
   birthdate DATE,
   role VARCHAR(50),
   creation_datetime TIMESTAMP DEFAULT current_timestamp,
   last_update_datetime TIMESTAMP
);
ALTER TABLE "user"
    ADD COLUMN sex VARCHAR(1);