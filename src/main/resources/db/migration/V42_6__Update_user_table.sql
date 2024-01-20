-- Create the ENUM type
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role') THEN
CREATE TYPE role AS ENUM (
        'MANAGER',
        'CLIENT'
    );
END IF;
END $$;


DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'sex') THEN
CREATE TYPE sex AS ENUM (
        'M',
        'F'
    );
END IF;
END $$;


-- Alter the data type and add a CHECK constraint for the role column
ALTER TABLE "user"
ALTER COLUMN role TYPE VARCHAR(50),
ADD CONSTRAINT check_role CHECK (role IN ('MANAGER', 'CLIENT'));

-- Add the sex column with a data type and CHECK constraint
ALTER TABLE "user"
ADD COLUMN sex VARCHAR(1) CHECK (sex IN ('M', 'F'));

-- Set the NOT NULL constraint for the creation_datetime column
ALTER TABLE "user"
ALTER COLUMN creation_datetime SET NOT NULL;

