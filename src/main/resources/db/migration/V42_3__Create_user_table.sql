CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

do
$$
begin
        if not exists(select from pg_type where typname = 'role') then
create type "role" as enum ('CLIENT', 'MANAGER');
end if;
        if not exists(select from pg_type where typname = 'sex') then
create type sex as enum ('M', 'F', 'OTHER');
end if;
end
$$;

CREATE TABLE IF NOT EXISTS "user" (
   id VARCHAR PRIMARY KEY DEFAULT UUID_GENERATE_V4(),
   firstname VARCHAR(100),
   lastname VARCHAR(100),
   mail VARCHAR(250) UNIQUE NOT NULL,
   birthdate DATE,
   role role NOT NULL,
    sex sex,
   creation_datetime TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp NOT NULL,
   last_update_datetime TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp
);

create index if not exists user_firstname_index on "user" (firstname);
create index if not exists user_lastname_index on "user" (lastname);