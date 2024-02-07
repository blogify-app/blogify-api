
do
$$
begin
        if not exists(select from pg_type where typname = 'user_status') then
create type user_status as enum ('ENABLED', 'BANISHED');
end if;
end
$$;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS  photo_url VARCHAR;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS  bio VARCHAR;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS  profile_banner_url VARCHAR;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS  username VARCHAR;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS  about TEXT;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS  status user_status;

create index if not exists user_username_id_index on "user" (username);
