-- Modify the data type and constraints for the user_id column
ALTER TABLE user_category
ALTER COLUMN user_id SET DATA TYPE VARCHAR,
ALTER COLUMN user_id SET NOT NULL;

-- Modify the data type and constraints for the category_id column
ALTER TABLE user_category
ALTER COLUMN category_id SET DATA TYPE VARCHAR,
ALTER COLUMN category_id SET NOT NULL;

-- Add foreign key constraints
ALTER TABLE user_category
ADD CONSTRAINT user_category_user_id_fk FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE user_category
ADD CONSTRAINT user_category_category_id_fk FOREIGN KEY (category_id) REFERENCES category (id);
