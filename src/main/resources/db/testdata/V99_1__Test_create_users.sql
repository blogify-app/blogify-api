INSERT INTO "user" (
    id,
    firstname,
    lastname,
    mail,
    birthdate,
    role,
    sex,
    firebase_id,
    creation_datetime,
    last_update_datetime,
    photo_key,
    bio,
    profile_banner_key,
    username,
    about,
    status,
    deleted
)
VALUES
    ('client1_id', 'Ryan', 'Andria', 'test@gmail.com', '01-01-1995', 'CLIENT', 'M', 'uQp7l4pzKuaaqCXjruhZw525pI23', '2000-01-01T08:12:20.00z', '2021-11-08T08:25:24.00Z', 'client1_profile_key', 'bio_client1', 'client1_banner_key', 'username_client1', 'about_client1', 'ENABLED', false),
    ('client2_id', 'Herilala', 'Raf', 'hei.hajatiana@gmail.com', '01-01-2002', 'CLIENT', 'F', 'W2O94puRphSI6HkaCP7kAjA9GFB2', '2002-01-01T08:12:20.00z', '2020-11-08T08:25:24.00Z', null, 'bio_client2', null, 'username_client2', 'about_client2', 'ENABLED', false),
    ('manager1_id', 'Vano', 'Andria', 'test+vano@hei.school', '01-01-2000', 'MANAGER', 'M', 'manager1_firebase_id', '2000-09-01T08:12:20.00z', '2021-08-08T08:25:24.00Z', null, 'bio_manager1', null, 'username_manager1', 'about_manager1', 'ENABLED', false);
