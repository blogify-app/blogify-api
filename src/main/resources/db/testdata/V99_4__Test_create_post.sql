INSERT INTO post (id, user_id, title, content, thumbnail_key, description, status, creation_datetime, last_update_datetime, deleted)
VALUES
    ('post1_id', 'client1_id', 'Premier Post', 'Contenu du premier post', 'post1_thumbnail_key', 'Description du premier post', 'DRAFT', '2000-09-01T08:12:20.00z', '2000-09-01T08:12:20.00z', false),
    ('post2_id', 'client2_id', 'Deuxième Post', 'Contenu du deuxième post', null, 'Description du deuxième post', 'DRAFT', '2000-09-01T08:12:20.00z', '2000-09-01T08:12:20.00z', false);
