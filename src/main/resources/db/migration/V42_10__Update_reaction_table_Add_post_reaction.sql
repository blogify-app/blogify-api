ALTER TABLE "reaction"
    ADD COLUMN post_id VARCHAR(255),
ADD CONSTRAINT fk_reaction_post
FOREIGN KEY (post_id) REFERENCES post(id);