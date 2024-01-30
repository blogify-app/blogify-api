ALTER TABLE picture
    ADD COLUMN post_id VARCHAR(255),
ADD CONSTRAINT fk_picture_post
FOREIGN KEY (post_id) REFERENCES post(id);

create index if not exists picture_post_id_index on picture (post_id);
