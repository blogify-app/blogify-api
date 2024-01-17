create table if not exists comment_reacts
(
    id UUID PRIMARY KEY,
    comment_id UUID REFERENCES comments(id),
    is_like BOOLEAN
);