create table if not exists comment_reaction
(
    id varchar primary key default uuid_generate_v4(),
    comment_id UUID REFERENCES comment(id),
    is_like BOOLEAN
);