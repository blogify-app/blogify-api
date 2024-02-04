package com.blogify.blogapi.integration.conf.MockData;

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.category1;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.category2;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client1;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client2;
import static com.blogify.blogapi.integration.conf.TestUtils.POST1_PICTURE1_URL;
import static com.blogify.blogapi.integration.conf.TestUtils.POST1_PICTURE2_URL;
import static com.blogify.blogapi.integration.conf.TestUtils.POST1_THUMBNAIL_URL;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostPicture;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;
import com.blogify.blogapi.endpoint.rest.model.ReactionStat;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PostMockData {

  public static final String POST1_ID = "post1_id";
  public static final String POST2_ID = "post2_id";
  public static final String PICTURE1_URL = "picture1_id";
  public static final String PICTURE2_URL = "picture1_id";
  public static final String CREATE_POST1_ID = "creat_post1_id";
  public static final String PICTURE1_ID = "picture1_id";
  public static final String PICTURE2_ID = "picture2_id";

  public static Post post1() {
    return new Post()
        .id(POST1_ID)
        .author(client1())
        .thumbnailUrl(POST1_THUMBNAIL_URL)
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
        .author(client2())
        .thumbnailUrl(null)
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
        .author(client2())
        .thumbnailUrl(null)
        .description("create description")
        .content("create content")
        .title("create Post")
        .status(PostStatus.DRAFT)
        .reactions(new ReactionStat().dislikes(BigDecimal.ZERO).likes(BigDecimal.ZERO))
        .creationDatetime(null)
        .updatedAt(null)
        .categories(List.of(category1()));
  }

  public static PostPicture postPicture1() {
    return new PostPicture()
        .id(PICTURE1_ID)
        .postId(POST1_ID)
        .placeholder(PICTURE1_ID)
        .url(POST1_PICTURE1_URL);
  }

  public static PostPicture postPicture2() {
    return new PostPicture()
        .id(PICTURE2_ID)
        .postId(POST1_ID)
        .placeholder(PICTURE2_ID)
        .url(POST1_PICTURE2_URL);
  }
}
