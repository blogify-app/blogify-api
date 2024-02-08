package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT2_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT3_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment1;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment2;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment3;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST2_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post1;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post2;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.getDislikeReactionPoint;
import static com.blogify.blogapi.integration.conf.TestUtils.getDislikeReactionPointInComment;
import static com.blogify.blogapi.integration.conf.TestUtils.getLikeReactionPoint;
import static com.blogify.blogapi.integration.conf.TestUtils.getLikeReactionPointInComment;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.CommentsApi;
import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = ReactionIT.ContextInitializer.class)
public class ReactionIT {
  @MockBean private FirebaseService firebaseServiceMock;
  @MockBean private S3Service s3Service;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, ReactionIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
    setUpS3Service(s3Service);
  }

  @Test
  void client_recto_post_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    ApiClient client2Client = apiClient(CLIENT2_TOKEN);
    PostingApi api2 = new PostingApi(client2Client);

    List<Post> allPostsBefore = api.getPosts(1, 10, null);

    Reaction post1DislikeByClient1 = api.reactToPostById(POST1_ID, ReactionType.DISLIKE);

    List<Post> allPosts1 = api.getPosts(1, 10, null);

    Reaction post2likeByClient2 = api2.reactToPostById(POST2_ID, ReactionType.LIKE);

    List<Post> allPosts2 = api.getPosts(1, 10, null);

    assertEquals(3, allPostsBefore.size());
    assertTrue(allPostsBefore.contains(post1()));
    assertTrue(allPostsBefore.contains(post2()));
    assertEquals(BigDecimal.valueOf(2), getLikeReactionPoint(allPostsBefore, POST1_ID));
    assertEquals(BigDecimal.valueOf(0), getDislikeReactionPoint(allPostsBefore, POST1_ID));
    assertEquals(BigDecimal.valueOf(0), getLikeReactionPoint(allPostsBefore, POST2_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPostsBefore, POST2_ID));

    assertEquals(3, allPosts1.size());
    assertEquals(BigDecimal.valueOf(1), getLikeReactionPoint(allPosts1, POST1_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts1, POST1_ID));
    assertEquals(BigDecimal.valueOf(0), getLikeReactionPoint(allPosts1, POST2_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts1, POST2_ID));

    assertEquals(3, allPosts2.size());
    assertEquals(BigDecimal.valueOf(1), getLikeReactionPoint(allPosts2, POST1_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts2, POST1_ID));
    assertEquals(BigDecimal.valueOf(1), getLikeReactionPoint(allPosts2, POST2_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts2, POST2_ID));
  }

  @Test
  @DirtiesContext
  void client_recto_comment_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    ApiClient client2Client = apiClient(CLIENT2_TOKEN);
    CommentsApi api2 = new CommentsApi(client2Client);

    List<Comment> allPost1CommentsBefore = api.getCommentsByPostId(POST1_ID, 1, 10);

    Reaction post1DislikeByClient1 =
        api.reactToCommentById(POST1_ID, COMMENT1_ID, ReactionType.LIKE);

    List<Comment> allPost1Comments1 = api.getCommentsByPostId(POST1_ID, 1, 10);

    Reaction post2likeByClient2 = api2.reactToCommentById(POST1_ID, COMMENT3_ID, ReactionType.LIKE);

    List<Comment> allPost1Comments2 = api.getCommentsByPostId(POST1_ID, 1, 10);

    assertEquals(3, allPost1CommentsBefore.size());
    assertTrue(allPost1CommentsBefore.contains(comment1()));
    assertTrue(allPost1CommentsBefore.contains(comment2()));
    assertTrue(allPost1CommentsBefore.contains(comment3()));
    assertEquals(
        BigDecimal.valueOf(1), getLikeReactionPointInComment(allPost1CommentsBefore, COMMENT1_ID));
    assertEquals(
        BigDecimal.valueOf(1),
        getDislikeReactionPointInComment(allPost1CommentsBefore, COMMENT1_ID));
    assertEquals(
        BigDecimal.valueOf(0), getLikeReactionPointInComment(allPost1CommentsBefore, COMMENT2_ID));
    assertEquals(
        BigDecimal.valueOf(1),
        getDislikeReactionPointInComment(allPost1CommentsBefore, COMMENT2_ID));
    assertEquals(
        BigDecimal.valueOf(0), getLikeReactionPointInComment(allPost1CommentsBefore, COMMENT3_ID));
    assertEquals(
        BigDecimal.valueOf(0),
        getDislikeReactionPointInComment(allPost1CommentsBefore, COMMENT3_ID));

    assertEquals(3, allPost1Comments1.size());
    assertEquals(
        BigDecimal.valueOf(2), getLikeReactionPointInComment(allPost1Comments1, COMMENT1_ID));
    assertEquals(
        BigDecimal.valueOf(0), getDislikeReactionPointInComment(allPost1Comments1, COMMENT1_ID));

    assertEquals(3, allPost1Comments2.size());
    assertEquals(
        BigDecimal.valueOf(1), getLikeReactionPointInComment(allPost1Comments2, COMMENT3_ID));
    assertEquals(
        BigDecimal.valueOf(0), getDislikeReactionPointInComment(allPost1Comments2, COMMENT3_ID));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}
