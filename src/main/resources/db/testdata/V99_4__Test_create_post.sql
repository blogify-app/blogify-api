INSERT INTO post (id, user_id, title, content, thumbnail_url, description, status, creation_datetime, last_update_datetime)
VALUES
    ('1', 'client1_id', 'Premier Post', 'Contenu du premier post', 'url_image1.jpg', 'Description du premier post', 'DRAFT', NOW(), NOW()),
    ('2', 'client2_id', 'Deuxième Post', 'Contenu du deuxième post', 'url_image2.jpg', 'Description du deuxième post', 'DRAFT', NOW(), NOW());
