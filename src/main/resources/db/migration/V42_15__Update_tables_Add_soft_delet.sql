ALTER TABLE "post"
    ADD COLUMN deleted boolean default false not null ;

create index if not exists pose_deleted_index on "post" (deleted);


ALTER TABLE "comment"
    ADD COLUMN deleted boolean default false not null ;

create index if not exists comment_deleted_index on "comment" (deleted);


ALTER TABLE "user"
    ADD COLUMN deleted boolean default false not null ;

create index if not exists user_deleted_index on "user" (deleted);


ALTER TABLE "category"
    ADD COLUMN deleted boolean default false not null ;

create index if not exists category_deleted_index on "category" (deleted);
