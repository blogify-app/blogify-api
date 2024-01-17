create table if not exists post_reaction
(
    id UUID PRIMARY KEY,
    id_post UUID REFERENCES post(id),
    type VARCHAR(255)
    );