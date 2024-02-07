DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user' AND column_name = 'profile_banner_url') THEN
        EXECUTE 'ALTER TABLE IF EXISTS "user" RENAME COLUMN  profile_banner_url to profile_banner_key';
END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user' AND column_name = 'photo_url') THEN
        EXECUTE 'ALTER TABLE IF EXISTS "user" RENAME COLUMN  photo_url to photo_key';
END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'post' AND column_name = 'thumbnail_url') THEN
        EXECUTE 'ALTER TABLE IF EXISTS "post" RENAME COLUMN  thumbnail_url to thumbnail_key';
END IF;
END $$;
