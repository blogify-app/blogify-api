do
    $$
        begin
            if not exists(select from pg_type where typname = 'type') then
            create type "type" as enum ('LIKE','DISLIKE');
            end if;
        end
    $$;

CREATE TABLE IF NOT EXISTS "reaction"
(
    id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    type type NOT NULL,
    creation_datetime TIMESTAMP DEFAULT NOW(),
    user_id VARCHAR NOT NULL CONSTRAINT reaction__user_id_fk REFERENCES "user"(id)
);