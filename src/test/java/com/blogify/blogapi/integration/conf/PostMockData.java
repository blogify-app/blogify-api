package com.blogify.blogapi.integration.conf;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;

import java.time.Instant;

public class PostMockData {

    private static final String POST1_ID = "post1_id";
    private static final String POST2_ID = "post2_id";

    public static Post post1(){
        return new Post()
                .id(POST1_ID)
                .thumbnailUrl("thumbnail_url_post1")
                .description("description_post1")
                .content("content_post1")
                .title("post1_title")
                .status(PostStatus.DRAFT)
                .creationDatetime(Instant.parse("2002-01-01T08:12:20.00Z"))
                .updatedAt(null);
    }
    public static Post post2(){
        return new Post()
                .id(POST2_ID)
                .thumbnailUrl("thumbnail_url_post2")
                .description("description_post2")
                .content("content_post2")
                .title("post2_title")
                .status(PostStatus.DRAFT)
                .creationDatetime(Instant.parse("2003-06-01T08:12:20.00Z"))
                .updatedAt(null);
    }
}
