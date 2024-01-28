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
    photo_url,
    bio,
    profile_banner_url,
    username,
    about,
    status,
    deleted
)
VALUES
    ('client1_id', 'Ryan', 'Andria', 'test+ryan@hei.school', '01-01-1995', 'CLIENT', 'M', 'client1_firebase_id', '2000-01-01T08:12:20.00z', '2021-11-08T08:25:24.00Z', 'photo_url_client1', 'bio_client1', 'banner_url_client1', 'username_client1', 'about_client1', 'ENABLED', false),
    ('client2_id', 'Herilala', 'Raf', 'test+herilala@hei.school', '01-01-2002', 'CLIENT', 'F', 'client2_firebase_id', '2002-01-01T08:12:20.00z', '2020-11-08T08:25:24.00Z', 'photo_url_client2', 'bio_client2', 'banner_url_client2', 'username_client2', 'about_client2', 'ENABLED', false),
    ('manager1_id', 'Vano', 'Andria', 'test+vano@hei.school', '01-01-2000', 'MANAGER', 'M', 'manager1_firebase_id', '2000-09-01T08:12:20.00z', '2021-08-08T08:25:24.00Z', 'photo_url_manager1', 'bio_manager1', 'banner_url_manager1', 'username_manager1', 'about_manager1', 'ENABLED', false);
