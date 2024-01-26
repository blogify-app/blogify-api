package com.blogify.blogapi.integration.conf.MockData;

import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client1;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client2;

import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.CommentStatus;
import com.blogify.blogapi.endpoint.rest.model.ReactionStat;
import java.math.BigDecimal;
import java.time.Instant;

public class CommentMockData {

  public static final String COMMENT1_ID = "comment1_id";
  public static final String COMMENT2_ID = "comment2_id";
  public static final String COMMENT3_ID = "comment3_id";

  public static final String CREATE_COMMENT1_ID = "creat_comment1_id";

  public static Comment comment1() {
    return new Comment()
        .id(COMMENT1_ID)
        .user(client1())
        .postId(POST1_ID)
        .content("this in the content1")
        .creationDatetime(Instant.parse("2000-09-01T08:12:20.00z"))
        .replyToId(null)
        .reactions(new ReactionStat().dislikes(BigDecimal.valueOf(1)).likes(BigDecimal.valueOf(1)))
        .status(CommentStatus.ENABLED);
  }

  public static Comment comment2() {
    return new Comment()
        .id(COMMENT2_ID)
        .user(client1())
        .postId(POST1_ID)
        .content("this in the content2")
        .creationDatetime(Instant.parse("2000-10-01T08:12:20.00z"))
        .replyToId(COMMENT1_ID)
        .reactions(new ReactionStat().dislikes(BigDecimal.valueOf(1)).likes(BigDecimal.valueOf(0)))
        .status(CommentStatus.ENABLED);
  }

  public static Comment comment3() {
    return new Comment()
        .id(COMMENT3_ID)
        .user(client2())
        .postId(POST1_ID)
        .content("this in the content3")
        .creationDatetime(Instant.parse("2000-11-01T08:12:20.00z"))
        .replyToId(null)
        .reactions(new ReactionStat().dislikes(BigDecimal.valueOf(0)).likes(BigDecimal.valueOf(0)))
        .status(CommentStatus.ENABLED);
  }

  public static Comment postToCreate() {
    return new Comment()
        .id(CREATE_COMMENT1_ID)
        .user(client2())
        .postId(POST1_ID)
        .content("this in the content created")
        .creationDatetime(null)
        .replyToId(null)
        .reactions(new ReactionStat().dislikes(BigDecimal.valueOf(0)).likes(BigDecimal.valueOf(0)))
        .status(CommentStatus.ENABLED);
  }
}
