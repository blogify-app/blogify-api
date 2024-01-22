CREATE TABLE IF NOT EXISTS "reaction"
(
    id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    type VARCHAR NOT NULL,
    creation_datetime TIMESTAMP DEFAULT NOW(),
    user_id VARCHAR NOT NULL CONSTRAINT reaction__user_id_fk REFERENCES "user"(id)
    --post_id VARCHAR NOT NULL CONSTRAINT reaction__post_id_fk REFERENCES "post"(id)
);