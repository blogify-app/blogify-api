package com.blogify.blogapi.integration.conf;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;

import java.time.Instant;

public class PostMockData {

    private static final String POST1_ID = "1";
    private static final String POST2_ID = "2";

    public static Post post1() {
        return new Post()
                .id(POST1_ID)
                .authorId("client1_id")
                .thumbnailUrl("url_image1.jpg")
                .description("Description du premier post")
                .content("Contenu du premier post")
                .title("Premier Post")
                .status(PostStatus.DRAFT)
                .creationDatetime(Instant.now())
                .updatedAt(Instant.now());
    }

    public static Post post2() {
        return new Post()
                .id(POST2_ID)
                .authorId("client2_id")
                .thumbnailUrl("url_image2.jpg")
                .description("Description du deuxième post")
                .content("Contenu du deuxième post")
                .title("Deuxième Post")
                .status(PostStatus.DRAFT)
                .creationDatetime(Instant.now())
                .updatedAt(Instant.now());
    }

}
