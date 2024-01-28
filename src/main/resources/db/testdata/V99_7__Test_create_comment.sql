insert into "comment" (id, content, creation_datetime, user_id, reply_to_id, post_id, status, deleted)
values
    ('comment1_id', 'this in the content1', '2000-09-01T08:12:20.00z', 'client1_id', null, 'post1_id', 'ENABLED', false),
    ('comment2_id', 'this in the content2', '2000-10-01T08:12:20.00z', 'client1_id', null, 'post1_id', 'ENABLED', false),
    ('comment3_id', 'this in the content3', '2000-11-01T08:12:20.00z', 'client2_id', null, 'post1_id', 'ENABLED', false);

update comment set reply_to_id = 'comment1_id' where id = 'comment2_id';
