create table if not exists comment_reaction
(
    id varchar primary key default uuid_generate_v4(),
    user_id varchar REFERENCES user(id),
    comment_id varchar REFERENCES comment(id),
    is_like BOOLEAN
);