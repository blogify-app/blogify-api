CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS "user" (
   id VARCHAR DEFAULT UUID_GENERATE_V4(),
   firstname VARCHAR(50),
   lastname VARCHAR(50),
   mail VARCHAR(50) NOT NULL,
   birthdate DATE,
   role VARCHAR(50),
   creation_datetime TIMESTAMP,
   last_update_datetime TIMESTAMP,
   PRIMARY KEY(id)
);