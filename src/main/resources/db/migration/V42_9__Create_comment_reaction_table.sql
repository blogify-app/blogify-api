create table if not exists comment_reaction
(
    id UUID PRIMARY KEY,
    comment_id UUID REFERENCES comment(id),
    is_like BOOLEAN
);