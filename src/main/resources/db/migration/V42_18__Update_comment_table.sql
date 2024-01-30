-- add constraint on post_id column
ALTER TABLE "comment"
ADD CONSTRAINT comment__post_id_fk FOREIGN KEY (post_id) REFERENCES "post"(id);