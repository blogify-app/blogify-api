package com.blogify.blogapi.integration.conf.MockData;

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.category1;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.category2;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT2_ID;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;
import com.blogify.blogapi.endpoint.rest.model.ReactionStat;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PostMockData {

  public static final String POST1_ID = "post1_id";
  public static final String POST2_ID = "post2_id";

  public static final String CREATE_POST1_ID = "creat_post1_id";

  public static Post post1() {
    return new Post()
        .id(POST1_ID)
        .authorId(CLIENT1_ID)
        .thumbnailUrl("url_image1.jpg")
        .description("Description du premier post")
        .content("Contenu du premier post")
        .title("Premier Post")
        .status(PostStatus.DRAFT)
        .categories(List.of(category1(), category2()))
        .reactions(new ReactionStat().likes(BigDecimal.valueOf(2)).dislikes(BigDecimal.ZERO))
        .creationDatetime(Instant.parse("2000-09-01T08:12:20.00z"))
        .updatedAt(Instant.parse("2000-09-01T08:12:20.00z"));
  }

  public static Post post2() {
    return new Post()
        .id(POST2_ID)
        .authorId(CLIENT2_ID)
        .thumbnailUrl("url_image2.jpg")
        .description("Description du deuxième post")
        .content("Contenu du deuxième post")
        .title("Deuxième Post")
        .status(PostStatus.DRAFT)
        .categories(List.of(category1()))
        .reactions(new ReactionStat().dislikes(BigDecimal.valueOf(1)).likes(BigDecimal.ZERO))
        .creationDatetime(Instant.parse("2000-09-01T08:12:20.00z"))
        .updatedAt(Instant.parse("2000-09-01T08:12:20.00z"));
  }

  public static Post postToCreate() {
    return new Post()
        .id(CREATE_POST1_ID)
        .authorId(CLIENT2_ID)
        .thumbnailUrl("url_image_creat.jpg")
        .description("create description")
        .content("create content")
        .title("create Post")
        .status(PostStatus.DRAFT)
        .reactions(new ReactionStat().dislikes(BigDecimal.ZERO).likes(BigDecimal.ZERO))
        .creationDatetime(null)
        .updatedAt(null)
        .categories(List.of(category1()));
  }
}
