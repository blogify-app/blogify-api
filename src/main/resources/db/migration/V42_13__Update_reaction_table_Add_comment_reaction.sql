ALTER TABLE "reaction"
    ADD COLUMN comment_id VARCHAR(255),
ADD CONSTRAINT fk_reaction_comment
FOREIGN KEY (comment_id) REFERENCES "comment"(id);