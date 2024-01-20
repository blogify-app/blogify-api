-- Drop the foreign key constraint on the user_id column
ALTER TABLE user_category
DROP CONSTRAINT user_category_user_id_fkey;

-- Drop the foreign key constraint on the category_id column
ALTER TABLE user_category
DROP CONSTRAINT user_category_category_id_fkey;


-- Modify the id data type and set a default value to UUID
-- set the creation_datetime to not null


ALTER TABLE category

ALTER COLUMN id SET DATA TYPE VARCHAR,
ALTER COLUMN id SET DEFAULT UUID_GENERATE_V4(),

ALTER COLUMN creation_datetime SET NOT NULL;