package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT2_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.CREATE_COMMENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment1;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment2;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment3;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.commentToCreate;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST2_ID;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.CommentsApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.CommentStatus;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.model.exception.BadRequestException;
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

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, CommentIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
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
  void client_write_ko() throws ApiException{
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    List<Comment> allCommentsBeforeCreate = api.getCommentsByPostId(POST1_ID, 1, 10);

    Comment invalidComment = new Comment();
    invalidComment.setContent("");
    invalidComment.setUser(null);
    invalidComment.setStatus(CommentStatus.DISABLED);

    assertThrows(ApiException.class, () -> {
      api.crupdateCommentById(POST1_ID, CREATE_COMMENT1_ID, invalidComment);
    });

    List<Comment> allCommentsAfterCreate = api.getCommentsByPostId(POST1_ID, 1, 10);

    assertEquals(3, allCommentsBeforeCreate.size());
    assertEquals(3, allCommentsAfterCreate.size());
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

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}
