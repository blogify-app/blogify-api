package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT2_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT3_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.CREATE_COMMENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment1;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment2;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment3;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.commentToCreate;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST2_ID;
import static com.blogify.blogapi.integration.conf.TestUtils.BAD_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.assertThrowsApiException;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.CommentsApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.CommentStatus;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
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
@ContextConfiguration(initializers = CommentIT.ContextInitializer.class)
public class CommentIT {
  @MockBean private FirebaseService firebaseServiceMock;
  @MockBean private S3Service s3Service;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, CommentIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
    setUpS3Service(s3Service);
  }

  @Test
  void client_read_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    Comment commentByPostId = api.getCommentById(POST1_ID, COMMENT2_ID);
    List<Comment> allPost1Comments = api.getCommentsByPostId(POST1_ID, 1, 10);

    List<Comment> allPost2Comments = api.getCommentsByPostId(POST2_ID, 1, 10);

    assertEquals("this in the content2", commentByPostId.getContent());
    assertEquals("post1_id", commentByPostId.getPostId());
    assertEquals(3, allPost1Comments.size());
    assertTrue(allPost1Comments.contains(comment1()));
    assertTrue(allPost1Comments.contains(comment2()));
    assertTrue(allPost1Comments.contains(comment3()));

    assertEquals(0, allPost2Comments.size());
  }

  @Test
  @DirtiesContext
  void client_write_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    String newContent = "new content";

    List<Comment> allCommentsBeforeUpdate = api.getCommentsByPostId(POST1_ID, 1, 10);

    Comment updateComment1 =
        api.crupdateCommentById(POST1_ID, COMMENT1_ID, comment1().content(newContent));

    List<Comment> allCommentsAfterUpdate = api.getCommentsByPostId(POST1_ID, 1, 10);

    Comment createdComment1 =
        api.crupdateCommentById(POST1_ID, CREATE_COMMENT1_ID, commentToCreate());

    List<Comment> allCommentsAfterCreate = api.getCommentsByPostId(POST1_ID, 1, 10);

    assertEquals(3, allCommentsBeforeUpdate.size());
    assertTrue(allCommentsBeforeUpdate.contains(comment1()));
    assertTrue(allCommentsBeforeUpdate.contains(comment2()));
    assertTrue(allCommentsBeforeUpdate.contains(comment3()));

    assertEquals(newContent, updateComment1.getContent());
    assertEquals(3, allCommentsAfterUpdate.size());
    assertTrue(allCommentsAfterUpdate.contains(comment1().content(newContent)));
    assertTrue(allCommentsAfterUpdate.contains(comment2()));
    assertTrue(allCommentsAfterUpdate.contains(comment3()));

    assertEquals(CREATE_COMMENT1_ID, createdComment1.getId());
    assertEquals(commentToCreate().getId(), createdComment1.getId());
    assertEquals(commentToCreate().getUser(), createdComment1.getUser());
    assertEquals(commentToCreate().getPostId(), createdComment1.getPostId());
    assertEquals(commentToCreate().getContent(), createdComment1.getContent());
    assertEquals(commentToCreate().getReplyToId(), createdComment1.getReplyToId());
    assertEquals(commentToCreate().getReactions(), createdComment1.getReactions());
    assertEquals(commentToCreate().getStatus(), createdComment1.getStatus());
    assertEquals(4, allCommentsAfterCreate.size());
  }

  @Test
  void client_write_ko() {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    assertThrowsApiException(
        "{\"type\":\"400 BAD_REQUEST\",\"message\":\"" + "Content is mandatory. " + "\"}",
        () -> {
          api.crupdateCommentById(POST1_ID, CREATE_COMMENT1_ID, commentToCreate().content(""));
        });

    assertThrowsApiException(
        "{\"type\":\"400 BAD_REQUEST\",\"message\":\"" + "Content is mandatory. " + "\"}",
        () -> {
          api.crupdateCommentById(POST1_ID, CREATE_COMMENT1_ID, commentToCreate().content(null));
        });

    assertThrowsApiException(
        "{\"type\":\"400 BAD_REQUEST\",\"message\":\"" + "Invalid Comment Status. " + "\"}",
        () -> {
          api.crupdateCommentById(
              POST1_ID, CREATE_COMMENT1_ID, commentToCreate().status(CommentStatus.DISABLED));
        });

    assertThrowsApiException(
        "{\"type\":\"400 BAD_REQUEST\",\"message\":\"" + "User is mandatory." + "\"}",
        () -> {
          api.crupdateCommentById(POST1_ID, CREATE_COMMENT1_ID, commentToCreate().user(null));
        });
  }

  @Test
  @DirtiesContext
  void client_delete_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);
    List<Comment> allCommentsBeforeDelete = api.getCommentsByPostId(POST1_ID, 1, 10);
    api.deleteCommentById(POST1_ID, COMMENT1_ID);
    List<Comment> allCommentsAfterDelete = api.getCommentsByPostId(POST1_ID, 1, 10);

    assertEquals(3, allCommentsBeforeDelete.size());
    assertTrue(allCommentsBeforeDelete.contains(comment1()));
    assertTrue(allCommentsBeforeDelete.contains(comment2()));
    assertTrue(allCommentsBeforeDelete.contains(comment3()));

    assertEquals(2, allCommentsAfterDelete.size());
    assertFalse(allCommentsAfterDelete.contains(comment1()));
    assertTrue(allCommentsAfterDelete.contains(comment2()));
    assertTrue(allCommentsAfterDelete.contains(comment3()));
  }

  @Test
  void read_comment_by_postId_ko() {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);
    String postId = POST1_ID;
    String commentId = randomUUID().toString();

    assertThrowsApiException(
        "{\"type\":\"404 NOT_FOUND\",\"message\":\"Resource of type Comment identified by "
            + commentId
            + " not found\"}",
        () -> api.getCommentById(postId, commentId));
  }

  @Test
  void not_authenticate_create_ko() {
    ApiClient client1Client = apiClient(BAD_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    assertThrowsApiException(
        "{\"type\":\"403 FORBIDDEN\",\"message\":\"Bearer token is expired or invalid\"}",
        () -> api.reactToCommentById(POST1_ID, COMMENT3_ID, ReactionType.DISLIKE));
  }

  @Test
  void not_authenticate_delete_ko() {
    ApiClient client1Client = apiClient(BAD_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    assertThrowsApiException(
        "{\"type\":\"403 FORBIDDEN\",\"message\":\"Bearer token is expired or invalid\"}",
        () -> api.deleteCommentById(POST1_ID, COMMENT2_ID));
  }

  @Test
  void other_client_delete_ko() {
    ApiClient client2Client = apiClient(CLIENT2_TOKEN);
    CommentsApi api = new CommentsApi(client2Client);

    ApiException exception =
        assertThrows(ApiException.class, () -> api.deleteCommentById(POST1_ID, COMMENT2_ID));

    assertTrue(exception.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}
